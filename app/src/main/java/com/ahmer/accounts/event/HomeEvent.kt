package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.SortBy

sealed class HomeEvent {
    data class OnDeleteClick(val userModel: UserModel) : HomeEvent()
    data class OnItemClick(val userModel: UserModel) : HomeEvent()
    data class OnSortBy(val sortBy: SortBy) : HomeEvent()
    data object OnAddClick : HomeEvent()
    data object OnUndoDeleteClick : HomeEvent()
}
