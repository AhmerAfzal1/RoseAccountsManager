package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.ahmer.accounts.database.repository.ExpenseRepository
import com.ahmer.accounts.state.ExpenseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) : ViewModel(), LifecycleObserver {

    private val _uiState: MutableStateFlow<ExpenseState> = MutableStateFlow(value = ExpenseState())
    val uiState: StateFlow<ExpenseState> = _uiState.asStateFlow()

    private fun allExpense() {
        expenseRepository.allExpenses().onEach { listExpense ->
            _uiState.update { state ->
                state.copy(allExpenses = listExpense)
            }
        }
    }

    init {
        allExpense()
    }
}