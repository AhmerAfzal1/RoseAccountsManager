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
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.state.TransState
import com.ahmer.accounts.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private var mDeletedTrans: TransModel? = null
    private var mLoadTransJob: Job? = null

    var userId: Long = 0

    fun onEvent(event: TransEvent) {
        when (event) {
            is TransEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedTrans = event.transModel
                    repository.delete(event.transModel)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = "Transaction id ${event.transModel.id} deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TransEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Navigate(route = ScreenRoutes.TransAddDialog + "?transId=${event.transModel.id}/transUserId=-1"))
                }
            }

            TransEvent.OnAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Navigate(ScreenRoutes.TransAddDialog + "?transId=-1/transUserId=$userId"))
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
        userId: Long, searchQuery: MutableState<String>
    ): Flow<ResultState<List<TransModel>>> {
        return repository.getAllTransByUserIdWithSearch(userId, searchQuery.value)
    }

    fun getAllUserTransactions() {
        mLoadTransJob?.cancel()
        mLoadTransJob =
            getAllTransactionWithSearch(userId, _searchQuery).onEach { resultState ->
                _uiState.update { transState -> transState.copy(getAllUsersTransList = resultState) }
            }.launchIn(viewModelScope)
    }

    init {
        savedStateHandle.get<Int>("transUserId")?.let { id ->
            Log.v(Constants.LOG_TAG, "Transaction user id: $id")
            userId = id.toLong()
            getAllUserTransactions()
        }
    }
}