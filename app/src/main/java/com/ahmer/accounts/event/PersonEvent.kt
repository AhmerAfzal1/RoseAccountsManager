package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.PersonsEntity

sealed class PersonEvent {
    data class OnAddEditTransaction(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnDelete(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnSearchTextChange(val searchQuery: String) : PersonEvent()
    data object OnAddEditPerson : PersonEvent()
    data object OnSettings : PersonEvent()
    data object OnUndoDeletePerson : PersonEvent()
}