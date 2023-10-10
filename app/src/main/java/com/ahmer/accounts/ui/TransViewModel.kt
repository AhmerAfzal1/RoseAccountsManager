package com.ahmer.accounts.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.database.repository.PersonRepository
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.ScreenRoutes
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
    private val personRepository: PersonRepository,
    private val transRepository: TransRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {
    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState: MutableStateFlow<TransState> = MutableStateFlow(value = TransState())
    val uiState: StateFlow<TransState> = _uiState.asStateFlow()

    private var getPersonsEntity: PersonsEntity = PersonsEntity()
    private var getTransSumModel: TransSumModel = TransSumModel()
    private var mDeletedTrans: TransEntity? = null
    private var mPersonId: MutableState<Int> = mutableIntStateOf(value = 0)

    fun onEvent(event: TransEvent) {
        when (event) {
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
                            route = ScreenRoutes.TransAddEditScreen + "?transId=${event.transEntity.id}/transPersonId=-1"
                        )
                    )
                }
            }

            is TransEvent.OnSearchTextChange -> {
                viewModelScope.launch {
                    _searchQuery.value = event.searchQuery
                }
            }

            TransEvent.OnAddClick -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        value = UiEvent.Navigate(
                            route = ScreenRoutes.TransAddEditScreen + "?transId=-1/transPersonId=${mPersonId.value}"
                        )
                    )
                }
            }

            TransEvent.OnUndoDeleteClick -> {
                mDeletedTrans?.let { transaction ->
                    viewModelScope.launch {
                        transRepository.insertOrUpdate(transaction)
                    }
                }
            }
        }
    }

    fun generatePdf(context: Context, uri: Uri) {
        transRepository.getAllTransByPersonIdForPdf(personId = getPersonsEntity.id)
            .filterNotNull()
            .onEach { transEntityList ->
                var isSuccessfully = false
                val mJob = CoroutineScope(Dispatchers.IO).launch {
                    isSuccessfully = PdfUtils.generatePdf(
                        context = context,
                        uri = uri,
                        transEntity = transEntityList,
                        transSumModel = getTransSumModel,
                        personName = getPersonsEntity.name
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

    private fun getPersonByIdData() {
        personRepository.getPersonById(personId = mPersonId.value).onEach { personsEntity ->
            getPersonsEntity = personsEntity!!
        }.launchIn(scope = viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllPersonsTransactions() {
        _searchQuery.flatMapLatest { search ->
            transRepository.getAllTransByPersonIdWithSearch(
                personId = mPersonId.value, searchQuery = search
            )
        }.onEach { transEntityList ->
            _uiState.update { transState -> transState.copy(getAllPersonsTransList = transEntityList) }
        }.launchIn(scope = viewModelScope)
    }

    private fun getAccountBalance() {
        transRepository.getAccountBalanceByPerson(mPersonId.value).onEach { resultState ->
            _uiState.update { it.copy(getPersonTransBalance = resultState) }
            getTransSumModel = resultState
        }.launchIn(scope = viewModelScope)
    }

    init {
        savedStateHandle.get<Int>("transPersonId")?.let { id ->
            Log.v(Constants.LOG_TAG, "Clicked on person id: $id for add transaction")
            mPersonId.value = id
            getAllPersonsTransactions()
            getAccountBalance()
            getPersonByIdData()
        }
    }
}