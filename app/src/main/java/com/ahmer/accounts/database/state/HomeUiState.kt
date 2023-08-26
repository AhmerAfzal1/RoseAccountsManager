package com.ahmer.accounts.database.state

data class HomeUiState(
    val usersData: UiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)