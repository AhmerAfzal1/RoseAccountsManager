package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.repository.ExpenseRepository
import com.ahmer.accounts.event.ExpenseEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.NavItems
import com.ahmer.accounts.state.ExpenseState
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState: MutableStateFlow<ExpenseState> = MutableStateFlow(value = ExpenseState())
    val uiState: StateFlow<ExpenseState> = _uiState.asStateFlow()

    private var mDeletedExpense: ExpenseEntity? = null

    fun onEvent(event: ExpenseEvent) {
        when (event) {
            ExpenseEvent.OnAdd -> {
                viewModelScope.launch {
                    _eventFlow.emit(value = UiEvent.Navigate(route = NavItems.ExpenseAddEdit.route))
                }
            }

            is ExpenseEvent.OnEdit -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.ExpenseAddEdit.route + "?expenseID=${event.expenseEntity.id}"
                        )
                    )
                }
            }

            is ExpenseEvent.OnDelete -> {
                viewModelScope.launch {
                    mDeletedExpense = event.expenseEntity
                    expenseRepository.delete(expenseEntity = event.expenseEntity)
                    _eventFlow.emit(
                        value = UiEvent.ShowSnackBar(message = "Expense deleted", action = "Undo")
                    )
                }
            }

            is ExpenseEvent.OnSearchTextChange -> {

            }

            ExpenseEvent.OnUndoDelete -> {
                mDeletedExpense?.let { expense ->
                    viewModelScope.launch {
                        expenseRepository.insertOrUpdate(expenseEntity = expense)
                    }
                }
            }
        }
    }

    private fun allExpense() {
        expenseRepository.allExpenses().onEach { listExpense ->
            _uiState.update { state ->
                state.copy(allExpenses = listExpense)
            }
        }.launchIn(scope = viewModelScope)
    }

    init {
        allExpense()
    }
}