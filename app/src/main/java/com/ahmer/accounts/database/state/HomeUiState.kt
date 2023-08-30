package com.ahmer.accounts.database.state

data class HomeUiState(
    val uiState: UiState,
    val isRefreshing: Boolean,
    val isError: Boolean
)