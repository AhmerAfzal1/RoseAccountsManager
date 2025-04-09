package com.ahmer.accounts.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.database.repository.TransactionRepository
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.NavItems
import com.ahmer.accounts.state.TransState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.PdfUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState: MutableStateFlow<TransState> = MutableStateFlow(value = TransState())
    val uiState: StateFlow<TransState> = _uiState.asStateFlow()

    private var mDeletedTrans: TransactionEntity? = null
    private var mPersonId: Int by mutableIntStateOf(value = 0)

    fun onEvent(event: TransEvent) {
        when (event) {
            TransEvent.OnAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.TransactionsAddEdit.route + "?transId=-1/transPersonId=${mPersonId}"
                        )
                    )
                }
            }

            is TransEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.TransactionsAddEdit.route + "?transId=${event.transactionEntity.id}/transPersonId=-1"
                        )
                    )
                }
            }

            is TransEvent.OnPersonEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.PersonAddEdit.route + "?personId=${event.personsEntity.id}"
                        )
                    )
                }
            }

            is TransEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedTrans = event.transactionEntity
                    transactionRepository.delete(event.transactionEntity)
                    _eventFlow.emit(
                        value = UiEvent.ShowSnackBar(
                            message = "Transaction id ${event.transactionEntity.id} deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TransEvent.OnSearchTextChange -> {
                viewModelScope.launch {
                    _searchQuery.value = event.searchQuery
                }
            }

            TransEvent.OnUndoDeleteClick -> {
                mDeletedTrans?.let { transaction ->
                    viewModelScope.launch {
                        transactionRepository.insertOrUpdate(transaction = transaction)
                    }
                }
            }
        }
    }

    fun generatePdf(
        context: Context,
        uri: Uri,
        person: PersonEntity,
        transSum: TransactionSumModel
    ) {
        Log.v(Constants.LOG_TAG, "Credit: ${transSum.creditSum}")
        Log.v(Constants.LOG_TAG, "Debit: ${transSum.debitSum}")
        transactionRepository.getTransactionsByPersonId(personId = person.id, sort = 1)
            .filterNotNull()
            .onEach { transEntityList ->
                Log.v(Constants.LOG_TAG, "List: ${transEntityList.first()}")
                var isSuccessfully = false
                val mJob = CoroutineScope(context = Dispatchers.IO).launch {
                    isSuccessfully = PdfUtils.generatePdf(
                        context = context,
                        uri = uri,
                        transactions = transEntityList,
                        sumModel = _uiState.value.transactionSumModel,
                        person = person
                    )
                }

                mJob.invokeOnCompletion {
                    viewModelScope.launch {
                        if (isSuccessfully) {
                            val mMsg = context.getString(R.string.toast_pdf_generated)
                            _eventFlow.emit(value = UiEvent.ShowToast(mMsg))
                        }
                    }
                }
            }
            .catch { e ->
                Log.e(
                    Constants.LOG_TAG,
                    context.getString(R.string.toast_pdf_generate_error, e.localizedMessage), e
                )
                FirebaseCrashlytics.getInstance().recordException(e)
            }
            .launchIn(scope = viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllPersonsTransactions() {
        _searchQuery.flatMapLatest { search ->
            transactionRepository.searchTransactions(personId = mPersonId, searchQuery = search)
        }.onEach { transactions ->
            _uiState.update { transState -> transState.copy(allTransactions = transactions) }
        }.launchIn(scope = viewModelScope)
    }

    private fun getAccountBalance() {
        transactionRepository.getBalanceByPerson(personId = mPersonId).onEach { result ->
            _uiState.update { it.copy(transactionSumModel = result) }
        }.launchIn(scope = viewModelScope)
    }

    init {
        savedStateHandle.get<Int>(key = "transPersonId")?.let { id ->
            Log.v(Constants.LOG_TAG, "Clicked on person id: $id for add transaction")
            mPersonId = id
            getAllPersonsTransactions()
            getAccountBalance()
        }
    }
}