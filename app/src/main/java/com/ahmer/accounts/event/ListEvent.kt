package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy

sealed class ListEvent {
    data class OnDeleteClick(val userModel: UserModel) : ListEvent()
    data class OnInfoClick(val userModel: UserModel) : ListEvent()
    data class OnItemClick(val userModel: UserModel) : ListEvent()
    data class OnSortBy(val sortBy: SortBy) : ListEvent()
    object OnAddClick : ListEvent()
    object OnUndoDeleteClick : ListEvent()
}
