package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy

sealed class UserEvent {
    data class OnAddTransactionClick(val userModel: UserModel) : UserEvent()
    data class OnDeleteClick(val userModel: UserModel) : UserEvent()
    data class OnEditClick(val userModel: UserModel) : UserEvent()
    data class OnSortBy(val sortBy: SortBy) : UserEvent()
    data object OnNewAddClick : UserEvent()
    data object OnUndoDeleteClick : UserEvent()
}