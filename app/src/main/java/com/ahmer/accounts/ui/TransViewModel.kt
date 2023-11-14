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
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.database.repository.TransRepository
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
    private val transRepository: TransRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState: MutableStateFlow<TransState> = MutableStateFlow(value = TransState())
    val uiState: StateFlow<TransState> = _uiState.asStateFlow()

    private var mDeletedTrans: TransEntity? = null
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

            is TransEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    mDeletedTrans = event.transEntity
                    transRepository.delete(event.transEntity)
                    _eventFlow.emit(
                        value = UiEvent.ShowSnackBar(
                            message = "Transaction id ${event.transEntity.id} deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TransEvent.OnEditClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = NavItems.TransactionsAddEdit.route + "?transId=${event.transEntity.id}/transPersonId=-1"
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
                        transRepository.insertOrUpdate(transEntity = transaction)
                    }
                }
            }
        }
    }

    fun generatePdf(context: Context, uri: Uri, person: PersonsEntity, transSum: TransSumModel) {
        Log.v(Constants.LOG_TAG, "Credit: ${transSum.creditSum}")
        Log.v(Constants.LOG_TAG, "Debit: ${transSum.debitSum}")
        transRepository.allTransactionByPersonId(personId = person.id, sort = 1)
            .filterNotNull()
            .onEach { transEntityList ->
                Log.v(Constants.LOG_TAG, "List: ${transEntityList.first()}")
                var isSuccessfully = false
                val mJob = CoroutineScope(context = Dispatchers.IO).launch {
                    isSuccessfully = PdfUtils.generatePdf(
                        context = context,
                        uri = uri,
                        transEntity = transEntityList,
                        transSumModel = _uiState.value.transSumModel,
                        personName = person.name
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
            transRepository.allTransactionsSearch(personId = mPersonId, searchQuery = search)
        }.onEach { transactions ->
            _uiState.update { transState -> transState.copy(allTransactions = transactions) }
        }.launchIn(scope = viewModelScope)
    }

    private fun getAccountBalance() {
        transRepository.balanceByPerson(personId = mPersonId).onEach { result ->
            _uiState.update { it.copy(transSumModel = result) }
        }.launchIn(scope = viewModelScope)
    }

    init {
        savedStateHandle.get<Int>("transPersonId")?.let { id ->
            Log.v(Constants.LOG_TAG, "Clicked on person id: $id for add transaction")
            mPersonId = id
            getAllPersonsTransactions()
            getAccountBalance()
        }
    }
}