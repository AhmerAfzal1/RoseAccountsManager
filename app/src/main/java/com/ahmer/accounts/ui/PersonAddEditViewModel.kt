package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.event.PersonAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.PersonAddEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonAddEditViewModel @Inject constructor(
    private val repository: PersonRepository, savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState: MutableStateFlow<PersonAddEditState> =
        MutableStateFlow(value = PersonAddEditState())
    val uiState: StateFlow<PersonAddEditState> = _uiState.asStateFlow()

    private val accountId: Int = savedStateHandle["personId"] ?: -1
    val titleBar get(): String = if (accountId == -1) "Add Account Data" else "Edit Account Data"

    private suspend fun sendError(error: Throwable) {
        _eventFlow.emit(UiEvent.ShowToast(message = error.localizedMessage ?: "Unknown error"))
    }

    private suspend fun sendToast(message: String) {
        _eventFlow.emit(UiEvent.ShowToast(message = message))
    }

    private fun updateAccount(transform: PersonEntity.() -> PersonEntity) {
        _uiState.update { state ->
            state.person?.let { state.copy(person = it.transform()) } ?: state
        }
    }

    private fun PersonEntity.clean() = copy(
        name = name.trim(),
        phone = phone.trim(),
        email = email.trim(),
        address = address.trim(),
        notes = notes.trim(),
        updated = if (accountId == -1) 0 else System.currentTimeMillis()
    )

    private fun validateAndSave() = viewModelScope.launch {
        val account = _uiState.value.person ?: run {
            sendToast(message = "Account data not loaded")
            return@launch
        }

        if (account.name.isBlank()) {
            sendToast(message = "Name cannot be empty")
            return@launch
        }

        try {
            repository.insertOrUpdate(account.clean())
            val added = "Account ${account.name.trim()} added successfully"
            val update = "Account ${account.name.trim()} updated successfully"
            _eventFlow.emitAll(
                listOf(
                    UiEvent.ShowToast(if (accountId == -1) added else update),
                    UiEvent.PopBackStack
                ).asFlow()
            )
        } catch (e: Exception) {
            sendError(error = e)
        }
    }

    fun onEvent(event: PersonAddEditEvent) {
        when (event) {
            is PersonAddEditEvent.OnNameChange -> updateAccount { copy(name = event.name) }
            is PersonAddEditEvent.OnPhoneChange -> updateAccount { copy(phone = event.phone) }
            is PersonAddEditEvent.OnEmailChange -> updateAccount { copy(email = event.email) }
            is PersonAddEditEvent.OnAddressChange -> updateAccount { copy(address = event.address) }
            is PersonAddEditEvent.OnNotesChange -> updateAccount { copy(notes = event.notes) }
            PersonAddEditEvent.OnSaveClick -> validateAndSave()
        }
    }

    private fun loadAccount() = viewModelScope.launch {
        repository.getPersonById(personId = accountId).catch { e -> sendError(error = e) }
            .collect { _uiState.value = PersonAddEditState(person = it) }
    }

    init {
        if (accountId != -1) loadAccount()
        else _uiState.value = PersonAddEditState(person = PersonEntity())
    }
}