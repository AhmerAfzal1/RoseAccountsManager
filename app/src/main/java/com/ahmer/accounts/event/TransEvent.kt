package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity

sealed class TransEvent {
    data class OnEditClick(val transEntity: TransEntity) : TransEvent()
    data class OnPersonEditClick(val personsEntity: PersonsEntity) : TransEvent()
    data class OnDeleteClick(val transEntity: TransEntity) : TransEvent()
    data class OnSearchTextChange(val searchQuery: String) : TransEvent()
    data object OnAddClick : TransEvent()
    data object OnUndoDeleteClick : TransEvent()
}