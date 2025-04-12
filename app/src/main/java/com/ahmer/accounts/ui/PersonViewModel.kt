package com.ahmer.accounts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.model.PersonBalanceModel
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.NavItems
import com.ahmer.accounts.state.AccountState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.DataStore
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repository: PersonRepository,
    private val dataStore: DataStore,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState: MutableStateFlow<AccountState> = MutableStateFlow(value = AccountState())
    val uiState: StateFlow<AccountState> = _uiState.asStateFlow()

    private var deletedAccount: PersonEntity? = null

    var persons: List<PersonBalanceModel> by mutableStateOf(value = emptyList())
        private set

    val currentSortOrder: StateFlow<SortOrder> = dataStore.getSortOrder.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = Constants.STATE_IN_STARTED_TIME),
        initialValue = SortOrder.Date(sortBy = SortBy.Descending)
    )

    fun updateSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            dataStore.updateSortOrder(sortOrder = sortOrder)
        }
    }

    /*fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.OnAddEditTransaction -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.Transactions.route + "?transPersonId=${event.personsEntity.id}"
                        )
                    )
                }
            }

            is PersonEvent.OnDelete -> {
                viewModelScope.launch {
                    mDeletedAccount = event.personsEntity
                    repository.delete(person = event.personsEntity)
                    _eventFlow.emit(
                        value = UiEvent.ShowSnackBar(
                            message = "${event.personsEntity.name} deleted", action = "Undo"
                        )
                    )
                }
            }

            is PersonEvent.OnSearchTextChange -> {
                viewModelScope.launch {
                    _searchQuery.value = event.searchQuery
                }
            }

            is PersonEvent.OnSettings -> {
                viewModelScope.launch {
                    _eventFlow.emit(value = UiEvent.Navigate(route = NavItems.Settings.route))
                }
            }

            PersonEvent.OnAddEditPerson -> {
                viewModelScope.launch {
                    _eventFlow.emit(value = UiEvent.Navigate(NavItems.PersonAddEdit.route))
                }
            }

            PersonEvent.OnUndoDeletePerson -> {
                mDeletedAccount?.let { person ->
                    viewModelScope.launch {
                        repository.insertOrUpdate(person = person)
                    }
                }
            }
        }
    }*/

    fun deleteAccount(person: PersonEntity) {
        viewModelScope.launch {
            onEvent(PersonEvent.OnDelete(personsEntity = person))
            showUndoSnackbar(name = person.name)
        }
    }

    private fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.OnDelete -> deleteAccount(event.personsEntity)
            is PersonEvent.OnSearchTextChange -> updateSearchQuery(event.searchQuery)
            PersonEvent.OnUndoDeletePerson -> undoDelete()
            PersonEvent.OnAddEditPerson -> navigateToAddEdit()
            is PersonEvent.OnAddEditTransaction -> navigateToTransactions(event.personsEntity.id)
            PersonEvent.OnSettings -> navigateToSettings()
        }
    }

    private suspend fun showUndoSnackbar(name: String) {
        _eventFlow.emit(
            UiEvent.ShowSnackBar(
                message = "$name deleted",
                action = "Undo"
            )
        )
    }

    private fun undoDelete() {
        deletedAccount?.let { account ->
            viewModelScope.launch {
                repository.insertOrUpdate(account)
                deletedAccount = null
            }
        }
    }

    private fun navigateToAddEdit() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.Navigate(NavItems.PersonAddEdit.route))
        }
    }

    private fun navigateToTransactions(accountId: Int) {
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.Navigate(route = "${NavItems.Transactions.route}?transPersonId=$accountId")
            )
        }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.Navigate(NavItems.Settings.route))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadAccounts() {
        combine(_searchQuery, currentSortOrder) { query, sortOrder ->
            _uiState.update { it.copy(isLoading = true, error = null) }
            Pair(first = query, second = sortOrder)
        }.flatMapLatest { (search, sortOrder) ->
            repository.searchPersons(query = search, sortOrder = sortOrder)
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.localizedMessage ?: "Unknown error")
                    }
                }
        }.onEach { accounts ->
            _uiState.update {
                it.copy(allAccounts = accounts, isLoading = false, error = null)
            }
        }.launchIn(scope = viewModelScope)
    }

    init {
        loadAccounts()
    }
}