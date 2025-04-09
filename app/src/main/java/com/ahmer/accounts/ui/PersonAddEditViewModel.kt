package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.PersonEntity
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

    private val _uiState: MutableStateFlow<PersonAddEditState> =
        MutableStateFlow(value = PersonAddEditState())
    val uiState: StateFlow<PersonAddEditState> = _uiState.asStateFlow()

    private var mPersonId: Int? = 0
    var titleBar by mutableStateOf(value = "Add Person Data")

    private var currentPerson: PersonEntity?
        get() {
            return _uiState.value.person
        }
        private set(value) {
            _uiState.update { personAddEditState ->
                personAddEditState.copy(person = value)
            }
        }

    init {
        savedStateHandle.get<Int>(key = "personId")?.let { personId ->
            Log.v(Constants.LOG_TAG, "Person id: $personId")
            mPersonId = personId
            if (personId != -1) {
                titleBar = "Edit Person Data"
                personRepository.getPersonById(personId = personId).onEach { person ->
                    _uiState.update { addEditState ->
                        currentPerson = person
                        addEditState.copy(person = person)
                    }
                }.launchIn(scope = viewModelScope)
            } else {
                currentPerson = PersonEntity()
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
                        var mPerson: PersonEntity? by mutableStateOf(value = PersonEntity())
                        var mMessage by mutableStateOf(value = "")
                        if (currentPerson!!.name.isEmpty()) {
                            _eventFlow.emit(value = UiEvent.ShowToast("The name can't be empty"))
                            return@launch
                        }
                        if (mPersonId == -1) {
                            mPerson = currentPerson?.let { person ->
                                PersonEntity(
                                    id = person.id,
                                    name = person.name.trim(),
                                    address = person.address.trim(),
                                    phone = person.phone.trim(),
                                    email = person.email.trim(),
                                    notes = person.notes.trim(),
                                )
                            }
                            mMessage = "${mPerson?.name?.trim()} added successfully!"
                        } else {
                            mPerson = currentPerson?.copy(
                                id = currentPerson!!.id,
                                name = currentPerson!!.name.trim(),
                                address = currentPerson!!.address.trim(),
                                phone = currentPerson!!.phone.trim(),
                                email = currentPerson!!.email.trim(),
                                notes = currentPerson!!.notes.trim(),
                                updated = System.currentTimeMillis()
                            )
                            mMessage = "${mPerson?.name?.trim()} updated successfully!"
                        }
                        personRepository.insertOrUpdate(person = mPerson!!)
                        _eventFlow.emit(value = UiEvent.PopBackStack)
                        _eventFlow.emit(value = UiEvent.ShowToast(message = mMessage))
                    } catch (e: Exception) {
                        val mError = "This person could not be added due to an unknown error"
                        _eventFlow.emit(
                            value = UiEvent.ShowToast(message = e.localizedMessage ?: mError)
                        )
                    }
                }
            }
        }
    }
}