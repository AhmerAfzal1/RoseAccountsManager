package com.ahmer.accounts.event

sealed class PersonAddEditEvent {
    data class OnAddressChange(val address: String) : PersonAddEditEvent()
    data class OnEmailChange(val email: String) : PersonAddEditEvent()
    data class OnNameChange(val name: String) : PersonAddEditEvent()
    data class OnNotesChange(val notes: String) : PersonAddEditEvent()
    data class OnPhoneChange(val phone: String) : PersonAddEditEvent()
    data object OnSaveClick : PersonAddEditEvent()
}