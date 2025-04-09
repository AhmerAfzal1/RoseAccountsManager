package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.database.repository.TransactionRepository
import com.ahmer.accounts.state.ReportState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.ConstantsChart
import com.ahmer.accounts.utils.DateUtils
import com.ahmer.accounts.utils.chart.bar.BarChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel(), LifecycleObserver {

    private val _activeFilter = mutableStateOf(ConstantsChart.THIS_WEEK)
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
    }
}