package com.ahmer.accounts.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.ahmer.accounts.R
import com.ahmer.accounts.dl.AppModule
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.DataStore
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    val currentTheme: StateFlow<ThemeMode> = dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = Constants.STATE_IN_STARTED_TIME),
        initialValue = ThemeMode.System
    )

    fun updateTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            dataStore.updateTheme(themeMode = themeMode)
        }
    }

    fun backupDatabase(context: Context, uri: Uri?) {
        val mJob = CoroutineScope(Dispatchers.IO).launch {
            val mDatabase = AppModule.providesDatabase(context = context)
            val mQuery = SimpleSQLiteQuery(query = "pragma wal_checkpoint(full)")
            mDatabase.adminDao().checkPoint(supportSQLiteQuery = mQuery)
            val mInputStream = context.getDatabasePath(Constants.DATABASE_NAME).inputStream()
            val mOutputStream = uri?.let { context.contentResolver.openOutputStream(it) }
            runCatching {
                mInputStream.use { input ->
                    mOutputStream?.use { output ->
                        input.copyTo(out = output)
                    }
                }
            }
        }

        mJob.invokeOnCompletion {
            viewModelScope.launch {
                _eventFlow.emit(
                    value = UiEvent.ShowToast(
                        context.getString(
                            R.string.toast_msg_db_backup,
                            uri?.let {
                                HelperUtils.getFileNameFromDatabase(context = context, uri = it)
                            }, uri?.path
                        )
                    )
                )
            }
        }
    }

    fun closeDatabase(context: Context) {
        AppModule.providesDatabase(context).close()
    }

    fun restoreDatabase(context: Context, uri: Uri?) {
        val mJob = CoroutineScope(Dispatchers.IO).launch {
            val mDatabase = AppModule.providesDatabase(context = context)
            val mQuery = SimpleSQLiteQuery(query = "pragma wal_checkpoint(full)")
            mDatabase.adminDao().checkPoint(supportSQLiteQuery = mQuery)
            mDatabase.close()
            val mInputStream = uri?.let { context.contentResolver.openInputStream(it) }
            val mOutputStream = context.getDatabasePath(Constants.DATABASE_NAME).outputStream()
            runCatching {
                mInputStream.use { input ->
                    mOutputStream.use { output ->
                        input?.copyTo(out = output)
                    }
                }
            }
        }

        mJob.invokeOnCompletion {
            viewModelScope.launch {
                _eventFlow.emit(
                    value = UiEvent.ShowToast(
                        context.getString(R.string.toast_msg_db_restored, uri?.let {
                            HelperUtils.getFileNameFromDatabase(context = context, uri = it)
                        })
                    )
                )
                delay(duration = 1.seconds)
                _eventFlow.emit(value = UiEvent.RelaunchApp)
            }
        }
    }
}