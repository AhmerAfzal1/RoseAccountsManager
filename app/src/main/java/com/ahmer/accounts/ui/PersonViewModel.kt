package com.ahmer.accounts.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.dl.AppModule
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.preferences.PreferencesFilter
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.state.PersonState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repository: PersonRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _preferences: Flow<PreferencesFilter> = preferencesManager.preferencesFlow

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(PersonState())
    val uiState = _uiState.asStateFlow()

    private var mDeletedPerson: PersonsEntity? = null
    private var mLoadPersonsJob: Job? = null
    private var mLoadPersonsTotalBalanceJob: Job? = null

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.OnAddTransactionClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(
                            route = ScreenRoutes.TransListScreen + "?transPersonId=${event.personsEntity.id}"
                        )
                    )
                }
            }

            is PersonEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedPerson = event.personsEntity
                    repository.delete(event.personsEntity)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "${event.personsEntity.name} deleted", action = "Undo"
                        )
                    )
                }
            }

            is PersonEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(
                            route = ScreenRoutes.PersonAddEditScreen + "?personId=${event.personsEntity.id}"
                        )
                    )
                }
            }

            is PersonEvent.OnSearchTextChange -> {
                viewModelScope.launch {
                    _searchQuery.value = event.searchQuery
                }
            }

            is PersonEvent.OnSortBy -> {
                viewModelScope.launch {
                    preferencesManager.updateSortOrder(event.sortBy)
                }
            }

            PersonEvent.OnNewAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Navigate(ScreenRoutes.PersonAddEditScreen))
                }
            }

            PersonEvent.OnUndoDeleteClick -> {
                mDeletedPerson?.let { person ->
                    viewModelScope.launch {
                        repository.insertOrUpdate(person)
                    }
                }
            }
        }
    }

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
                        "${
                            uri?.let { HelperUtils.getFileNameFromDatabase(context, it) }
                        } successfully backed up to $uri"
                    )
                )
            }
        }
    }

    fun closeDatabase(context: Context) = AppModule.providesDatabase(context).close()

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
                _eventFlow.emit(UiEvent.ShowToast("${
                    uri?.let {
                        HelperUtils.getFileNameFromDatabase(context, it)
                    }
                } restored. App will relaunch now"))
                delay(1.seconds)
                _eventFlow.emit(UiEvent.RelaunchApp)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllPersonsData() {
        mLoadPersonsJob?.cancel()
        mLoadPersonsJob = combine(_searchQuery, _preferences) { query, preferences ->
            Pair(query, preferences)
        }.flatMapLatest { (search, preference) ->
            repository.getAllPersonsByFilter(search, preference.sortBy)
        }.onEach { resultState ->
            _uiState.update { personState -> personState.copy(getAllPersonsList = resultState) }
        }.launchIn(viewModelScope)
    }

    private fun getAllPersonsBalance() {
        mLoadPersonsTotalBalanceJob?.cancel()
        mLoadPersonsTotalBalanceJob = repository.getAllAccountsBalance().onEach { resultState ->
            _uiState.update { balState -> balState.copy(getAllPersonsBalance = resultState) }
        }.launchIn(viewModelScope)
    }

    init {
        getAllPersonsData()
        getAllPersonsBalance()
    }
}