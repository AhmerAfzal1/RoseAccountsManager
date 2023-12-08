package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.PersonsEntity

sealed class PersonEvent {
    data class OnAddTransactionClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnDeleteClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnSearchTextChange(val searchQuery: String) : PersonEvent()
    data object OnNewAddClick : PersonEvent()
    data object OnSettingsClick : PersonEvent()
    data object OnUndoDeleteClick : PersonEvent()
}