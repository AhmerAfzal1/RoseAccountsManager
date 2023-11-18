package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.TransEntity

sealed class TransAddEditEvent {
    data class OnAmountChange(val amount: String) : TransAddEditEvent()
    data class OnDateChange(val date: Long) : TransAddEditEvent()
    data class OnDeleteClick(val transEntity: TransEntity) : TransAddEditEvent()
    data class OnDescriptionChange(val description: String) : TransAddEditEvent()
    data class OnTypeChange(val type: String) : TransAddEditEvent()
    data object OnSaveClick : TransAddEditEvent()
}