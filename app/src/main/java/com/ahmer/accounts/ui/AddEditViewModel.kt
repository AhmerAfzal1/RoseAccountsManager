package com.ahmer.accounts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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

    var data by mutableStateOf<UserModel?>(null)
        private set

    var name by mutableStateOf("")
        private set

    var address by mutableStateOf("")
        private set

    var phone by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var notes by mutableStateOf("")
        private set

    private val userId = savedStateHandle.get<Int>("userId")

    init {
        if (userId != -1) {
            viewModelScope.launch {
                repository.getUserById(userId!!)?.let { user ->
                    name = user.name ?: ""
                    address = user.address ?: ""
                    phone = user.phone ?: ""
                    email = user.email ?: ""
                    notes = user.notes ?: ""
                    this@AddEditViewModel.data = user
                }
            }
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
                            UiEvent.ShowSnackBar(
                                message = "The name can't be empty"
                            )
                        )
                        return@launch
                    }

                    val user = if (userId == -1) {
                        UserModel(
                            name = name,
                            address = address,
                            phone = phone,
                            email = email,
                            notes = notes,
                        )
                    } else {
                        data?.copy(
                            id = data?.id,
                            name = name,
                            address = address,
                            phone = phone,
                            email = email,
                            notes = notes,
                            modified = System.currentTimeMillis()
                        )
                    }
                    if (user != null) {
                        repository.insertOrUpdate(user)
                    }

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

    fun insertOrUpdateUser(userModel: UserModel) = viewModelScope.launch {
        repository.insertOrUpdate(userModel)
    }
}