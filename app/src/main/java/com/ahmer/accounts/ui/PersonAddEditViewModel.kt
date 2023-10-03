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
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.event.PersonAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.PersonAddEditState
import com.ahmer.accounts.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAddEditViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(value = PersonAddEditState())
    val uiState: StateFlow<PersonAddEditState> = _uiState.asStateFlow()

    private var mPersonId: Int? = 0
    var titleBar by mutableStateOf(value = "Add Person Data")

    var currentPerson: PersonsEntity?
        get() {
            return _uiState.value.getPersonDetails.let {
                if (it is ResultState.Success) it.data else null
            }
        }
        private set(value) {
            _uiState.update { personAddEditState ->
                personAddEditState.copy(getPersonDetails = ResultState.Success(value))
            }
        }

    init {
        savedStateHandle.get<Int>("personId")?.let { personId ->
            Log.v(Constants.LOG_TAG, "Person id: $personId")
            mPersonId = personId
            if (personId != -1) {
                titleBar = "Edit Person Data"
                personRepository.getPersonById(personId = personId).onEach { resultState ->
                    _uiState.update { addEditState ->
                        if (resultState is ResultState.Success) {
                            currentPerson = resultState.data
                        }
                        addEditState.copy(getPersonDetails = resultState)
                    }
                }.launchIn(scope = viewModelScope)
            } else {
                currentPerson = PersonsEntity()
            }
        }
    }

    fun onEvent(event: PersonAddEditEvent) {
        when (event) {
            is PersonAddEditEvent.OnAddressChange -> {
                currentPerson = currentPerson?.copy(address = event.address)
            }

            is PersonAddEditEvent.OnEmailChange -> {
                //if (!Patterns.EMAIL_ADDRESS.matcher(event.email).matches())
                currentPerson = currentPerson?.copy(email = event.email)
            }

            is PersonAddEditEvent.OnNameChange -> {
                currentPerson = currentPerson?.copy(name = event.name)
            }

            is PersonAddEditEvent.OnNotesChange -> {
                currentPerson = currentPerson?.copy(notes = event.notes)
            }

            is PersonAddEditEvent.OnPhoneChange -> {
                currentPerson = currentPerson?.copy(phone = event.phone)
            }

            PersonAddEditEvent.OnSaveClick -> {
                viewModelScope.launch {
                    try {
                        var mPerson: PersonsEntity? by mutableStateOf(value = null)
                        var mMessage by mutableStateOf(value = "")
                        if (currentPerson!!.name.isEmpty()) {
                            _eventFlow.emit(UiEvent.ShowToast("The name can't be empty"))
                            return@launch
                        }
                        if (mPersonId == -1) {
                            mPerson = currentPerson?.let { person ->
                                PersonsEntity(
                                    id = person.id,
                                    name = person.name,
                                    address = person.address,
                                    phone = person.phone,
                                    email = person.email,
                                    notes = person.notes,
                                )
                            }
                            mMessage = "${mPerson?.name} added successfully!"
                        } else {
                            mPerson = currentPerson?.copy(
                                id = currentPerson!!.id,
                                name = currentPerson!!.name,
                                address = currentPerson!!.address,
                                phone = currentPerson!!.phone,
                                email = currentPerson!!.email,
                                notes = currentPerson!!.notes,
                                updated = System.currentTimeMillis()
                            )
                            mMessage = "${mPerson?.name} updated successfully!"
                        }
                        personRepository.insertOrUpdate(mPerson!!)
                        _eventFlow.emit(UiEvent.SaveSuccess)
                        _eventFlow.emit(UiEvent.ShowToast(mMessage))
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowToast(
                                message = e.localizedMessage ?: "Person couldn't be added"
                            )
                        )
                    }
                }
            }
        }
    }
}