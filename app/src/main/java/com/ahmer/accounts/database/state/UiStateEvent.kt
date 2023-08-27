package com.ahmer.accounts.database.state

data class UiStateEvent(
    val usersData: UiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)