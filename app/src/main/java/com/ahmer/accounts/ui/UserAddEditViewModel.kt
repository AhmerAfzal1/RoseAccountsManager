package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.event.UserAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.UserAddEditState
import com.ahmer.accounts.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddEditViewModel @Inject constructor(
    private val repository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(UserAddEditState())
    val uiState = _uiState.asStateFlow()

    private var mLoadUsersJob: Job? = null
    private var mUserId: Int? = 0

    var titleBar by mutableStateOf("Add User Data")

    var currentUser: UserModel?
        get() {
            return _uiState.value.getUserDetails.let {
                if (it is ResultState.Success) it.data else null
            }
        }
        private set(value) {
            _uiState.update {
                it.copy(getUserDetails = ResultState.Success(value))
            }
        }

    init {
        savedStateHandle.get<Int>("userId")?.let { id ->
            Log.v(Constants.LOG_TAG, "User id: $id")
            mUserId = id
            if (id != -1) {
                titleBar = "Edit User Data"
                mLoadUsersJob?.cancel()
                mLoadUsersJob = repository.getUserById(id).onEach { resultState ->
                    _uiState.update { addEditState ->
                        if (resultState is ResultState.Success) {
                            currentUser = resultState.data
                        }
                        addEditState.copy(getUserDetails = resultState)
                    }
                }.launchIn(viewModelScope)
            } else {
                currentUser = UserModel()
            }
        }
    }

    fun onEvent(event: UserAddEditEvent) {
        when (event) {
            is UserAddEditEvent.OnAddressChange -> {
                currentUser = currentUser?.copy(address = event.address)
            }

            is UserAddEditEvent.OnEmailChange -> {
                currentUser = currentUser?.copy(email = event.email)
            }

            is UserAddEditEvent.OnNameChange -> {
                currentUser = currentUser?.copy(name = event.name)
            }

            is UserAddEditEvent.OnNotesChange -> {
                currentUser = currentUser?.copy(notes = event.notes)
            }

            is UserAddEditEvent.OnPhoneChange -> {
                currentUser = currentUser?.copy(phone = event.phone)
            }

            UserAddEditEvent.OnSaveClick -> {
                viewModelScope.launch {
                    try {
                        var mUser: UserModel? by mutableStateOf(null)
                        var mMessage by mutableStateOf("")
                        if (currentUser!!.name.isEmpty()) {
                            _eventFlow.emit(UiEvent.ShowToast("The name can't be empty"))
                            return@launch
                        }
                        if (mUserId == -1) {
                            mUser = currentUser?.let { user ->
                                UserModel(
                                    id = user.id,
                                    name = user.name,
                                    address = user.address,
                                    phone = user.phone,
                                    email = user.email,
                                    notes = user.notes,
                                )
                            }
                            mMessage = "${mUser?.name} added successfully!"
                        } else {
                            mUser = currentUser?.copy(
                                id = currentUser!!.id,
                                name = currentUser!!.name,
                                address = currentUser!!.address,
                                phone = currentUser!!.phone,
                                email = currentUser!!.email,
                                notes = currentUser!!.notes,
                                modified = System.currentTimeMillis()
                            )
                            mMessage = "${mUser?.name} updated successfully!"
                        }
                        repository.insertOrUpdate(mUser!!)
                        _eventFlow.emit(UiEvent.SaveSuccess)
                        _eventFlow.emit(UiEvent.ShowToast(mMessage))
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowToast(
                                message = e.localizedMessage ?: "User couldn't be added"
                            )
                        )
                    }
                }
            }
        }
    }
}