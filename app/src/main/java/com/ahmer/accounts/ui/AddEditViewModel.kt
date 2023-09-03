package com.ahmer.accounts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.core.event.AddEditEvent
import com.ahmer.accounts.core.event.UiEvent
import com.ahmer.accounts.usecase.user.UserUseCase
import com.ahmer.accounts.utils.InvalidUsersException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val useCase: UserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private var mGetUserModelData: UserModel? by mutableStateOf(null)
    private var userId: Int? = 0

    var address by mutableStateOf("")
    var email by mutableStateOf("")
    var name by mutableStateOf("")
    var notes by mutableStateOf("")
    var phone by mutableStateOf("")
    var titleBar by mutableStateOf("Add User Data")

    init {
        savedStateHandle.get<Int>("userId")?.let { id ->
            userId = id
            if (id != -1) {
                titleBar = "Edit User Data"
                viewModelScope.launch {
                    useCase.getUserByIdUseCase(id)?.also { user ->
                        name = user.name ?: ""
                        address = user.address ?: ""
                        phone = user.phone ?: ""
                        email = user.email ?: ""
                        notes = user.notes ?: ""
                        this@AddEditViewModel.mGetUserModelData = user
                    }
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
                    try {
                        var mUser: UserModel? by mutableStateOf(null)
                        var mMessage by mutableStateOf("")
                        if (userId == -1) {
                            mUser = UserModel(
                                id = mGetUserModelData?.id,
                                name = name,
                                address = address,
                                phone = phone,
                                email = email,
                                notes = notes,
                            )
                            mMessage = "User added successfully!"
                        } else {
                            mUser = mGetUserModelData!!.copy(
                                id = mGetUserModelData?.id,
                                name = name,
                                address = address,
                                phone = phone,
                                email = email,
                                notes = notes,
                                modified = System.currentTimeMillis()
                            )
                            mMessage = "User updated successfully!"
                        }
                        useCase.addUserUseCase(mUser!!)
                        _eventFlow.emit(UiEvent.SaveUserSuccess)
                        _eventFlow.emit(UiEvent.ShowToast(mMessage))
                    } catch (e: InvalidUsersException) {
                        _eventFlow.emit(
                            UiEvent.ShowToast(message = e.message ?: "User couldn't be added")
                        )
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowToast(message = e.message ?: "User couldn't be added")
                        )
                    }
                }
            }
        }
    }
}