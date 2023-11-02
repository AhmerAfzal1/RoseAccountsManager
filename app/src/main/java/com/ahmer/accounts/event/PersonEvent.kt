package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.utils.SortOrder

sealed class PersonEvent {
    data class OnAddTransactionClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnDeleteClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnEditClick(val personsEntity: PersonsEntity) : PersonEvent()
    data class OnSearchTextChange(val searchQuery: String) : PersonEvent()
    data class OnSortBy(val sortOrder: SortOrder) : PersonEvent()
    data object OnNewAddClick : PersonEvent()
    data object OnSettingsClick : PersonEvent()
    data object OnUndoDeleteClick : PersonEvent()
}