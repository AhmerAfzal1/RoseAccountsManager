package com.rose.account.database.state

data class HomeUiState(
    val userData: UiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)