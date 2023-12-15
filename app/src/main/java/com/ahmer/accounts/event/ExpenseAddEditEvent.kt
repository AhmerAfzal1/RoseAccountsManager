package com.ahmer.accounts.event

sealed class ExpenseAddEditEvent {
    data class OnAmountChange(val amount: String) : ExpenseAddEditEvent()
    data class OnCategoryChange(val category: String) : ExpenseAddEditEvent()
    data class OnDescriptionChange(val description: String) : ExpenseAddEditEvent()
    data class OnDateChange(val date: Long) : ExpenseAddEditEvent()
    data class OnTypeChange(val type: String) : ExpenseAddEditEvent()
    data object OnSave : ExpenseAddEditEvent()
}