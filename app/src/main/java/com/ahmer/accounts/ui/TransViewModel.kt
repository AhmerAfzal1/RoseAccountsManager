package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.state.TransState
import com.ahmer.accounts.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransViewModel @Inject constructor(
    private val repository: TransRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _searchQuery: MutableState<String> = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _uiState = MutableStateFlow(TransState())
    val uiState = _uiState.asStateFlow()

    private var mDeletedTrans: TransEntity? = null
    private var mLoadAllTransByIdJob: Job? = null
    private var mLoadAllTransJob: Job? = null

    var personId: MutableState<Int> = mutableStateOf(0)

    fun onEvent(event: TransEvent) {
        when (event) {
            is TransEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedTrans = event.transEntity
                    repository.delete(event.transEntity)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Transaction id ${event.transEntity.id} deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TransEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(
                            route = ScreenRoutes.TransAddEditScreen +
                                    "?transId=${event.transEntity.id}/transPersonId=-1"
                        )
                    )
                }
            }

            TransEvent.OnAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.Navigate(
                            route = ScreenRoutes.TransAddEditScreen +
                                    "?transId=-1/transPersonId=${personId.value}"
                        )
                    )
                }
            }

            TransEvent.OnUndoDeleteClick -> {
                mDeletedTrans?.let { transaction ->
                    viewModelScope.launch {
                        repository.insertOrUpdate(transaction)
                    }
                }
            }
        }
    }

    private fun getAllTransactionWithSearch(
        personId: Int, searchQuery: MutableState<String>
    ): Flow<ResultState<List<TransEntity>>> {
        return repository.getAllTransByPersonIdWithSearch(personId, searchQuery.value)
    }

    fun getAllPersonsTransactions() {
        mLoadAllTransJob?.cancel()
        mLoadAllTransJob =
            getAllTransactionWithSearch(personId.value, _searchQuery).onEach { resultState ->
                _uiState.update { transState -> transState.copy(getAllPersonsTransList = resultState) }
            }.launchIn(viewModelScope)
    }

    private fun getAccountBalanceByPerson() {
        mLoadAllTransByIdJob?.cancel()
        mLoadAllTransByIdJob =
            repository.getAccountBalanceByPerson(personId.value).onEach { resultState ->
                _uiState.update { it.copy(getPersonTransBalance = resultState) }
            }.launchIn(viewModelScope)
    }

    init {
        savedStateHandle.get<Int>("transPersonId")?.let { id ->
            Log.v(Constants.LOG_TAG, "Clicked on person id: $id for add transaction")
            personId.value = id
            getAllPersonsTransactions()
            getAccountBalanceByPerson()
        }
    }
}