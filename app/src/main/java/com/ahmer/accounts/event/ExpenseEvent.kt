package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.ExpenseEntity

sealed class ExpenseEvent {
    data class OnEditClick(val expenseEntity: ExpenseEntity) : ExpenseEvent()
    data class OnDeleteClick(val expenseEntity: ExpenseEntity) : ExpenseEvent()
    data class OnSearchTextChange(val searchQuery: String) : ExpenseEvent()
    data object OnAddClick : ExpenseEvent()
    data object OnUndoDeleteClick : ExpenseEvent()
}