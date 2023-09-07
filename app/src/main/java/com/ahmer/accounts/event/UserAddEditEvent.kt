package com.ahmer.accounts.event

sealed class UserAddEditEvent {
    data class OnAddressChange(val address: String) : UserAddEditEvent()
    data class OnEmailChange(val email: String) : UserAddEditEvent()
    data class OnNameChange(val name: String) : UserAddEditEvent()
    data class OnNotesChange(val notes: String) : UserAddEditEvent()
    data class OnPhoneChange(val phone: String) : UserAddEditEvent()
    data object OnSaveClick : UserAddEditEvent()
}
