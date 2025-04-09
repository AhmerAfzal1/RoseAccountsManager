package com.ahmer.accounts.event

import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.entity.TransactionEntity

sealed class TransEvent {
    data class OnEditClick(val transactionEntity: TransactionEntity) : TransEvent()
    data class OnPersonEditClick(val personsEntity: PersonEntity) : TransEvent()
    data class OnDeleteClick(val transactionEntity: TransactionEntity) : TransEvent()
    data class OnSearchTextChange(val searchQuery: String) : TransEvent()
    data object OnAddClick : TransEvent()
    data object OnUndoDeleteClick : TransEvent()
}