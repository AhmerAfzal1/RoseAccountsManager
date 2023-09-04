package com.ahmer.accounts.event

sealed class AddEditEvent {
    data class OnAddressChange(val address: String) : AddEditEvent()
    data class OnEmailChange(val email: String) : AddEditEvent()
    data class OnNameChange(val name: String) : AddEditEvent()
    data class OnNotesChange(val notes: String) : AddEditEvent()
    data class OnPhoneChange(val phone: String) : AddEditEvent()
    data object OnSaveClick : AddEditEvent()
}
