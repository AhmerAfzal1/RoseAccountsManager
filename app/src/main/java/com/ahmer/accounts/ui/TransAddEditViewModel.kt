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
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.TransAddEditState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperFunctions
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
class TransAddEditViewModel @Inject constructor(
    private val repository: TransRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(TransAddEditState())
    val uiState = _uiState.asStateFlow()

    private var mLoadTransJob: Job? = null
    private var mPersonId: Int? = 0
    private var mTransId: Int? = 0

    var titleBar by mutableStateOf("Add Transaction")
    var titleButton by mutableStateOf("Save")

    var currentTransaction: TransEntity?
        get() {
            return _uiState.value.getTransDetails.let {
                if (it is ResultState.Success) it.data else null
            }
        }
        private set(value) {
            _uiState.update {
                it.copy(getTransDetails = ResultState.Success(value))
            }
        }

    init {
        savedStateHandle.get<Int>("transPersonId")?.let { personId ->
            Log.v(Constants.LOG_TAG, "Get person id for add in transaction, id: $personId")
            mPersonId = personId
        }
        savedStateHandle.get<Int>("transId")?.let { transId ->
            Log.v(Constants.LOG_TAG, "Get transaction id: $transId")
            mTransId = transId
            if (transId != -1) {
                titleBar = "Edit Transaction"
                titleButton = "Update"
                mLoadTransJob?.cancel()
                mLoadTransJob = repository.getAllTransById(transId).onEach { resultState ->
                    _uiState.update { addEditState ->
                        if (resultState is ResultState.Success) {
                            currentTransaction = resultState.data
                        }
                        addEditState.copy(getTransDetails = resultState)
                    }
                }.launchIn(viewModelScope)
            } else {
                currentTransaction = TransEntity(
                    date = HelperFunctions.getDateTime(
                        System.currentTimeMillis(), Constants.DATE_PATTERN
                    )
                )
            }
        }
    }

    fun onEvent(event: TransAddEditEvent) {
        when (event) {
            is TransAddEditEvent.OnAmountChange -> {
                currentTransaction = currentTransaction?.copy(amount = event.amount)
            }

            is TransAddEditEvent.OnDateChange -> {
                currentTransaction = currentTransaction?.copy(date = event.date)
            }

            is TransAddEditEvent.OnDescriptionChange -> {
                currentTransaction = currentTransaction?.copy(description = event.description)
            }

            is TransAddEditEvent.OnTypeChange -> {
                currentTransaction = currentTransaction?.copy(type = event.type)
            }

            TransAddEditEvent.OnSaveClick -> {
                save()
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            try {
                var mTransaction: TransEntity? by mutableStateOf(TransEntity())
                if (currentTransaction!!.type.isEmpty()) {
                    _eventFlow.emit(UiEvent.ShowToast("Please select credit or debit type"))
                    return@launch
                }
                if (currentTransaction!!.amount.isEmpty()) {
                    _eventFlow.emit(UiEvent.ShowToast("Amount cannot be empty"))
                    return@launch
                }

                if (mTransId == -1) {
                    mTransaction = currentTransaction?.let { transaction ->
                        TransEntity(
                            id = transaction.id,
                            personId = mPersonId!!,
                            date = transaction.date,
                            type = transaction.type,
                            description = transaction.description,
                            amount = transaction.amount
                        )
                    }
                } else {
                    mTransaction = currentTransaction?.copy(
                        id = currentTransaction!!.id,
                        personId = currentTransaction!!.personId,
                        date = currentTransaction!!.date,
                        type = currentTransaction!!.type,
                        description = currentTransaction!!.description,
                        amount = currentTransaction!!.amount
                    )
                }
                repository.insertOrUpdate(mTransaction!!)
                _eventFlow.emit(UiEvent.SaveSuccess)
            } catch (e: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowToast(e.localizedMessage ?: "Transaction couldn't be added")
                )
            }
        }
    }
}