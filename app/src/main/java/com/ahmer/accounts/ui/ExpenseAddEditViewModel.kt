package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.repository.ExpenseRepository
import com.ahmer.accounts.event.ExpenseAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.ExpenseAddEditState
import com.ahmer.accounts.utils.Constants
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
class ExpenseAddEditViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(value = ExpenseAddEditState())
    val uiState: StateFlow<ExpenseAddEditState> = _uiState.asStateFlow()

    private var mExpenseId: Int? = 0
    var isEditMode: Boolean by mutableStateOf(value = false)

    private var currentExpense: ExpenseEntity?
        get() {
            return _uiState.value.expense
        }
        private set(value) {
            _uiState.update { expenseAddEditState ->
                expenseAddEditState.copy(expense = value)
            }
        }

    fun onEvent(event: ExpenseAddEditEvent) {
        when (event) {
            is ExpenseAddEditEvent.OnAmountChange -> {
                currentExpense = currentExpense?.copy(amount = event.amount)
            }

            is ExpenseAddEditEvent.OnCategoryChange -> {
                currentExpense = currentExpense?.copy(category = event.category)
            }

            is ExpenseAddEditEvent.OnDateChange -> {
                currentExpense = currentExpense?.copy(date = event.date)
            }

            is ExpenseAddEditEvent.OnDescriptionChange -> {
                currentExpense = currentExpense?.copy(description = event.description)
            }

            is ExpenseAddEditEvent.OnTypeChange -> {
                currentExpense = currentExpense?.copy(type = event.type)
            }

            ExpenseAddEditEvent.OnSave -> saveData()
        }
    }

    private fun saveData() {
        viewModelScope.launch {
            try {
                var mExpense: ExpenseEntity? by mutableStateOf(value = ExpenseEntity())
                if (currentExpense!!.type.isEmpty()) {
                    _eventFlow.emit(value = UiEvent.ShowToast("Please select income or expense type"))
                    return@launch
                }
                if (currentExpense!!.amount.isEmpty()) {
                    _eventFlow.emit(value = UiEvent.ShowToast("Amount cannot be empty"))
                    return@launch
                }
                if (mExpenseId == -1) {
                    mExpense = currentExpense?.let { expense ->
                        ExpenseEntity(
                            id = expense.id,
                            date = expense.date,
                            type = expense.type,
                            category = expense.category,
                            amount = expense.amount.trim(),
                            description = expense.description.trim(),
                        )
                    }
                } else {
                    mExpense = currentExpense?.copy(
                        id = currentExpense!!.id,
                        date = currentExpense!!.date,
                        type = currentExpense!!.type,
                        category = currentExpense!!.category,
                        amount = currentExpense!!.amount.trim(),
                        description = currentExpense!!.description.trim(),
                    )
                }
                expenseRepository.insertOrUpdate(expenseEntity = mExpense!!)
                _eventFlow.emit(value = UiEvent.PopBackStack)
            } catch (e: Exception) {
                val mError = "The expense could not be added due to an unknown error"
                _eventFlow.emit(value = UiEvent.ShowToast(message = e.localizedMessage ?: mError))
            }
        }
    }

    init {
        isEditMode = false
        savedStateHandle.get<Int>(key = "expenseID")?.let { expenseId ->
            Log.v(Constants.LOG_TAG, "Get expense id: $expenseId")
            mExpenseId = expenseId
            if (mExpenseId != -1) {
                isEditMode = true
                expenseRepository.expenseById(expenseId = expenseId).onEach { expenseEntity ->
                    _uiState.update { addEditState ->
                        currentExpense = expenseEntity
                        addEditState.copy(expense = expenseEntity)
                    }
                }.launchIn(scope = viewModelScope)
            } else {
                currentExpense = ExpenseEntity(date = System.currentTimeMillis())
            }
        }
    }
}