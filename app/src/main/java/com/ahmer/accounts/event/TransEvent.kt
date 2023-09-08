package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.TransModel

sealed class TransEvent {
    data class OnDeleteClick(val transModel: TransModel) : TransEvent()
    data class OnEditClick(val transModel: TransModel) : TransEvent()
    data object OnAddClick : TransEvent()
    data object OnUndoDeleteClick : TransEvent()
}