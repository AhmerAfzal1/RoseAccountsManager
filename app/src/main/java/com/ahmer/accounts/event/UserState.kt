package com.ahmer.accounts.event

import com.ahmer.accounts.database.model.UserModel

data class UserState(
    val getAllUsersList: List<UserModel> = emptyList(),
    val isLoading: Boolean = false
)

