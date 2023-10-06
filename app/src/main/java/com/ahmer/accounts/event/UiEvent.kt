package com.ahmer.accounts.event

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackBar(val message: String, val action: String = "") : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
    data object RelaunchApp : UiEvent()
    data object SaveSuccess : UiEvent()
    data object ThemeUpdated : UiEvent()
}