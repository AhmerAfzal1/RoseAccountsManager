package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity

sealed class ExpenseEvent {
    data class OnEditClick(val expenseEntity: ExpenseEntity) : ExpenseEvent()
    data class OnDeleteClick(val expenseEntity: ExpenseEntity) : ExpenseEvent()
    data class OnSearchTextChange(val searchQuery: String) : ExpenseEvent()
    data object OnAddClick : ExpenseEvent()
    data object OnUndoDeleteClick : ExpenseEvent()
}