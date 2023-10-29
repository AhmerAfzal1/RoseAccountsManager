package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.navigation.NavItems
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.PersonState
import com.ahmer.accounts.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val dataStore: DataStore,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState: MutableStateFlow<PersonState> = MutableStateFlow(value = PersonState())
    val uiState: StateFlow<PersonState> = _uiState.asStateFlow()

    private var mDeletedPerson: PersonsEntity? = null

    fun onEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.OnAddTransactionClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.Transactions.route + "?transPersonId=${event.personsEntity.id}"
                        )
                    )
                }
            }

            is PersonEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedPerson = event.personsEntity
                    personRepository.delete(event.personsEntity)
                    _eventFlow.emit(
                        value = UiEvent.ShowSnackBar(
                            message = "${event.personsEntity.name} deleted", action = "Undo"
                        )
                    )
                }
            }

            is PersonEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.PersonAddEdit.route + "?personId=${event.personsEntity.id}"
                        )
                    )
                }
            }

            is PersonEvent.OnSearchTextChange -> {
                viewModelScope.launch {
                    _searchQuery.value = event.searchQuery
                }
            }

            is PersonEvent.OnSettingsClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(value = UiEvent.Navigate(route = NavItems.Settings.route))
                }
            }

            is PersonEvent.OnSortBy -> {
                viewModelScope.launch {
                    dataStore.updateSortOrder(event.sortOrder)
                }
            }

            PersonEvent.OnNewAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(value = UiEvent.Navigate(NavItems.PersonAddEdit.route))
                }
            }

            PersonEvent.OnUndoDeleteClick -> {
                mDeletedPerson?.let { person ->
                    viewModelScope.launch {
                        personRepository.insertOrUpdate(personsEntity = person)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllPersonsData() {
        combine(_searchQuery, dataStore.getSortOrder) { query, dataStore ->
            Pair(first = query, second = dataStore)
        }.flatMapLatest { (search, sortOrder) ->
            personRepository.getAllPersonsByFilter(searchQuery = search, sortOrder = sortOrder)
        }.onEach { resultState ->
            _uiState.update { personState -> personState.copy(getAllPersonsList = resultState) }
        }.launchIn(scope = viewModelScope)
    }

    init {
        getAllPersonsData()
    }
}