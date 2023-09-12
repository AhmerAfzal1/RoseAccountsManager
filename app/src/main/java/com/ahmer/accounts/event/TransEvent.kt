package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.TransEntity

sealed class TransEvent {
    data class OnDeleteClick(val transEntity: TransEntity) : TransEvent()
    data class OnEditClick(val transEntity: TransEntity) : TransEvent()
    data object OnAddClick : TransEvent()
    data object OnUndoDeleteClick : TransEvent()
}