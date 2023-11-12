package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.TransAddEditState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
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
class TransAddEditViewModel @Inject constructor(
    private val transRepository: TransRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(value = TransAddEditState())
    val uiState: StateFlow<TransAddEditState> = _uiState.asStateFlow()

    private var mPersonId: Int? = 0
    private var mTransId: Int? = 0

    var titleBar by mutableStateOf(value = "Add Transaction")
    var titleButton by mutableStateOf(value = "Save")

    private var currentTransaction: TransEntity?
        get() {
            return _uiState.value.transaction
        }
        private set(value) {
            _uiState.update { transAddEditState ->
                transAddEditState.copy(transaction = value)
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
                transRepository.transactionById(transId).onEach { transEntity ->
                    _uiState.update { addEditState ->
                        currentTransaction = transEntity
                        addEditState.copy(transaction = transEntity)
                    }
                }.launchIn(scope = viewModelScope)
            } else {
                currentTransaction = TransEntity(
                    date = HelperUtils.getDateTime(
                        time = System.currentTimeMillis(), pattern = Constants.DATE_PATTERN
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

            TransAddEditEvent.OnSaveClick -> save()
        }
    }

    private fun save() {
        viewModelScope.launch {
            try {
                var mTransaction: TransEntity? by mutableStateOf(value = TransEntity())
                if (currentTransaction!!.type.isEmpty()) {
                    _eventFlow.emit(value = UiEvent.ShowToast("Please select credit or debit type"))
                    return@launch
                }
                if (currentTransaction!!.amount.isEmpty()) {
                    _eventFlow.emit(value = UiEvent.ShowToast("Amount cannot be empty"))
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
                transRepository.insertOrUpdate(transEntity = mTransaction!!)
                _eventFlow.emit(value = UiEvent.PopBackStack)
            } catch (e: Exception) {
                val mError = "The transaction could not be added due to an unknown error"
                _eventFlow.emit(value = UiEvent.ShowToast(message = e.localizedMessage ?: mError))
            }
        }
    }
}