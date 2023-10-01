package com.ahmer.accounts.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.ahmer.accounts.R
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.dl.AppModule
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val personRepository: PersonRepository,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _isLoadingSplash: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoadingSplash: StateFlow<Boolean> = _isLoadingSplash.asStateFlow()

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    fun backupDatabase(context: Context, uri: Uri?) {
        val mJob = CoroutineScope(Dispatchers.IO).launch {
            val mDatabase = AppModule.providesDatabase(context)
            val mQuery = SimpleSQLiteQuery("pragma wal_checkpoint(full)")
            mDatabase.adminDao().checkPoint(mQuery)
            val mInputStream = context.getDatabasePath(Constants.DATABASE_NAME).inputStream()
            val mOutputStream = uri?.let { context.contentResolver.openOutputStream(it) }
            runCatching {
                mInputStream.use { input ->
                    mOutputStream?.use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }

        mJob.invokeOnCompletion {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ShowToast(
                        context.getString(
                            R.string.toast_msg_db_backup,
                            uri?.let { HelperUtils.getFileNameFromDatabase(context, it) }, uri?.path
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
            val mDatabase = AppModule.providesDatabase(context)
            val mQuery = SimpleSQLiteQuery("pragma wal_checkpoint(full)")
            mDatabase.adminDao().checkPoint(mQuery)
            mDatabase.close()
            val mInputStream = uri?.let { context.contentResolver.openInputStream(it) }
            val mOutputStream = context.getDatabasePath(Constants.DATABASE_NAME).outputStream()
            runCatching {
                mInputStream.use { input ->
                    mOutputStream.use { output ->
                        input?.copyTo(output)
                    }
                }
            }
        }

        mJob.invokeOnCompletion {
            viewModelScope.launch {
                _eventFlow.emit(
                    UiEvent.ShowToast(
                        context.getString(R.string.toast_msg_db_restored, uri?.let {
                            HelperUtils.getFileNameFromDatabase(context, it)
                        })
                    )
                )
                delay(1.seconds)
                _eventFlow.emit(UiEvent.RelaunchApp)
            }
        }
    }

    private fun getAllPersonsBalance() {
        personRepository.getAllAccountsBalance().onEach { transSumModel ->
            _uiState.update { balState -> balState.copy(getAllPersonsBalance = transSumModel) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            delay(1.seconds)
            _isLoadingSplash.value = false
        }
    }

    init {
        getAllPersonsBalance()
    }
}