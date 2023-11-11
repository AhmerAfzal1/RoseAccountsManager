package com.ahmer.accounts.event

sealed class TransAddEditEvent {
    data class OnAmountChange(val amount: String) : TransAddEditEvent()
    data class OnDateChange(val date: String) : TransAddEditEvent()
    data class OnDescriptionChange(val description: String) : TransAddEditEvent()
    data class OnTypeChange(val type: String) : TransAddEditEvent()
    data object OnSaveClick : TransAddEditEvent()
}