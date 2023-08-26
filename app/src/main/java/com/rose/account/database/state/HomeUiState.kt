package com.rose.account.database.state

data class HomeUiState(
    val usersData: UiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)