package com.ahmer.accounts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepositoryImp
import com.ahmer.accounts.event.AddEditEvent
import com.ahmer.accounts.event.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: UserRepositoryImp,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val mUserId = savedStateHandle.get<Int>("userId")
    private var userModelData by mutableStateOf<UserModel?>(null)

    var address by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var notes by mutableStateOf("")
    var phone by mutableStateOf("")
    var titleBar by mutableStateOf("")

    init {
        if (mUserId != -1) {
            viewModelScope.launch {
                titleBar = "Add User Data"
                repository.getUserById(mUserId!!)?.let { user ->
                    name = user.name ?: ""
                    address = user.address ?: ""
                    phone = user.phone ?: ""
                    email = user.email ?: ""
                    notes = user.notes ?: ""
                    this@AddEditViewModel.userModelData = user
                }
            }
        } else {
            titleBar = "Edit User Data"
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.OnAddressChange -> {
                address = event.address
            }

            is AddEditEvent.OnEmailChange -> {
                email = event.email
            }

            is AddEditEvent.OnNameChange -> {
                name = event.name
            }

            is AddEditEvent.OnNotesChange -> {
                notes = event.notes
            }

            is AddEditEvent.OnPhoneChange -> {
                phone = event.phone
            }

            AddEditEvent.OnSaveClick -> {
                viewModelScope.launch {
                    if (name.isEmpty()) {
                        sendUiEvent(
                            UiEvent.ShowSnackBar(message = "The name must not be empty")
                        )
                        return@launch
                    }

                    val mUser = if (mUserId == -1) {
                        UserModel(
                            name = name,
                            address = address,
                            phone = phone,
                            email = email,
                            notes = notes,
                        )
                    } else {
                        userModelData!!.copy(
                            id = userModelData?.id,
                            name = name,
                            address = address,
                            phone = phone,
                            email = email,
                            notes = notes,
                            modified = System.currentTimeMillis()
                        )
                    }

                    repository.insertOrUpdate(mUser)
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}