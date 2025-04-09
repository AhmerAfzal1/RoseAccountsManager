package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.PersonEntity

sealed class PersonEvent {
    data class OnAddEditTransaction(val personsEntity: PersonEntity) : PersonEvent()
    data class OnDelete(val personsEntity: PersonEntity) : PersonEvent()
    data class OnSearchTextChange(val searchQuery: String) : PersonEvent()
    data object OnAddEditPerson : PersonEvent()
    data object OnSettings : PersonEvent()
    data object OnUndoDeletePerson : PersonEvent()
}