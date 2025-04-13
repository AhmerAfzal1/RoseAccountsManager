package com.ahmer.accounts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.database.repository.TransactionRepository
import com.ahmer.accounts.state.ReportState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.ConstantsChart
import com.ahmer.accounts.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: TransactionRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<ReportState> = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    private val _activeFilter: MutableStateFlow<String> = MutableStateFlow(ConstantsChart.THIS_WEEK)
    val activeFilter: StateFlow<String> = _activeFilter.asStateFlow()

    private val today = DateUtils.formatDate(date = LocalDate.now())
    private val yesterday = DateUtils.previousDay(dateString = today)
    private val thisWeek = DateUtils.getWeekDates(dateString = today)
    private val lastWeek = DateUtils.getPastWeekData()
    private val thisMonth = DateUtils.getMonthDates(dateString = today)

    fun onChangeActiveFilter(filter: String) {
        _activeFilter.value = filter
        loadData(filter = filter)
    }

    private fun getTransactionsFlow(filter: String): Flow<List<TransactionEntity>> = when (filter) {
        ConstantsChart.TODAY -> repository.getTransactionsByDate(date = today)
        ConstantsChart.YESTERDAY -> repository.getTransactionsByDate(date = yesterday)
        ConstantsChart.THIS_WEEK -> repository.getTransactionsBetweenDates(dateRange = thisWeek)
        ConstantsChart.LAST_7_DAYS -> repository.getTransactionsBetweenDates(dateRange = lastWeek)
        ConstantsChart.THIS_MONTH -> repository.getTransactionsBetweenDates(dateRange = thisMonth)
        ConstantsChart.ALL -> repository.getAllTransactions()
        else -> flowOf(emptyList())
    }

    private fun calculateBalances(transactions: List<TransactionEntity>): Pair<Double, Double> {
        val (credits, debits) = transactions.partition { it.type == Constants.TYPE_CREDIT }
        return credits.sumOf { it.amount.toDouble() } to debits.sumOf { it.amount.toDouble() }
    }

    private fun loadData(filter: String) {
        getTransactionsFlow(filter = filter)
            .onEach { transactions ->
                val (credit, debit) = calculateBalances(transactions = transactions)

                _state.update {
                    it.copy(
                        transactions = transactions,
                        transactionSum = TransactionSumModel(creditSum = credit, debitSum = debit)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateActiveFilter(filter: String) {
        _activeFilter.value = filter
        loadData(filter)
    }

    init {
        loadData(ConstantsChart.THIS_WEEK)
    }

    /*private val _activeFilter = mutableStateOf(ConstantsChart.THIS_WEEK)
    val activeFilter: State<String> = _activeFilter

    private val _barDataList = mutableStateOf<List<BarChartData.Bar>>(emptyList())
    val barDataList: State<List<BarChartData.Bar>> = _barDataList

    private val _graph: MutableStateFlow<ReportState> = MutableStateFlow(ReportState())
    val graph: StateFlow<ReportState> = _graph.asStateFlow()

    private val mToday = DateUtils.formatDate(date = LocalDate.now())
    private val mYesterday = DateUtils.previousDay(dateString = mToday)
    private val mThisWeek = DateUtils.getWeekDates(dateString = mToday)
    private val mLastWeek = DateUtils.getPastWeekData()
    private val mThisMonth = DateUtils.getMonthDates(dateString = mToday)

    private fun filtered(filter: String): Flow<List<TransactionEntity>> {
        return when (filter) {
            ConstantsChart.TODAY -> {
                transactionRepository.getTransactionsByDate(date = mToday)
            }

            ConstantsChart.YESTERDAY -> {
                transactionRepository.getTransactionsByDate(date = mYesterday)
            }

            ConstantsChart.THIS_WEEK -> {
                transactionRepository.getTransactionsBetweenDates(dateRange = mThisWeek)
            }

            ConstantsChart.LAST_7_DAYS -> {
                transactionRepository.getTransactionsBetweenDates(dateRange = mLastWeek)
            }

            ConstantsChart.THIS_MONTH -> {
                transactionRepository.getTransactionsBetweenDates(dateRange = mThisMonth)
            }

            ConstantsChart.ALL -> {
                transactionRepository.getAllTransactions()
            }

            else -> {
                flow { emptyList<TransactionEntity>() }
            }
        }
    }

    fun onChangeActiveFilter(filter: String) {
        _activeFilter.value = filter
        data(filter = filter)
    }

    fun onChangeBarDataList(data: List<BarChartData.Bar>) {
        _barDataList.value = data
    }

    private fun data(filter: String) {
        filtered(filter = filter).onEach { list ->
            _graph.update { state ->
                state.copy(allTransactions = list)
            }
        }.launchIn(scope = viewModelScope)
    }

    init {
        data(filter = ConstantsChart.THIS_WEEK)
        Log.v(Constants.LOG_TAG, "Today days: $mToday")
        Log.v(Constants.LOG_TAG, "Yesterday days: $mYesterday")
        Log.v(Constants.LOG_TAG, "Week days: $mThisWeek")
        Log.v(Constants.LOG_TAG, "Last 7 days: $mLastWeek")
        Log.v(Constants.LOG_TAG, "This month days: $mThisMonth")
    }*/
}