package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.ExpenseEntity

sealed class ExpenseEvent {
    data class OnEdit(val expenseEntity: ExpenseEntity) : ExpenseEvent()
    data class OnDelete(val expenseEntity: ExpenseEntity) : ExpenseEvent()
    data class OnSearchTextChange(val searchQuery: String) : ExpenseEvent()
    data object OnAdd : ExpenseEvent()
    data object OnUndoDelete : ExpenseEvent()
}