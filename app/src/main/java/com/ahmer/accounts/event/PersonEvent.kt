package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.utils.SortBy

sealed class PersonEvent {
    data class OnAddTransactionClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnDeleteClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnEditClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnSearchTextChange(val searchQuery: String) : PersonEvent()
    data class OnSortBy(val sortBy: SortBy) : PersonEvent()
    data object OnNewAddClick : PersonEvent()
    data object OnUndoDeleteClick : PersonEvent()
}