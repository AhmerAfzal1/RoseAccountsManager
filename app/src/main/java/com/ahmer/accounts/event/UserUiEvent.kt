package com.ahmer.accounts.event

sealed class UserUiEvent {
    data class Navigate(val route: String) : UserUiEvent()
    data class ShowSnackBar(val message: String, val action: String = "") : UserUiEvent()
    data class ShowToast(val message: String) : UserUiEvent()
    data object SaveUserSuccess : UserUiEvent()
}