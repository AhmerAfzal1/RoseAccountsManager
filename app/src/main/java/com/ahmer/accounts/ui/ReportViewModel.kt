package com.ahmer.accounts.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.database.entity.TransEntity
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.state.ReportState
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.ConstantsChart
import com.ahmer.accounts.utils.DateUtils
import com.github.tehras.charts.bar.BarChartData
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
    private val transRepository: TransRepository,
) : ViewModel(), LifecycleObserver {

    private val _activeFilter = mutableStateOf(ConstantsChart.THIS_WEEK)
    val activeFilter: State<String> = _activeFilter

    private val _barDataList = mutableStateOf<List<BarChartData.Bar>>(emptyList())
    val barDataList: State<List<BarChartData.Bar>> = _barDataList

    private val _graph: MutableStateFlow<ReportState> = MutableStateFlow(ReportState())
    val graph: StateFlow<ReportState> = _graph.asStateFlow()

    private val todayDate = DateUtils.generateFormatDate(date = LocalDate.now())
    private val yesterdayDate = DateUtils.previousDay(date = todayDate)
    private val weekDates = DateUtils.getWeekDates(dateString = todayDate)
    private val monthDates = DateUtils.getMonthDates(dateString = todayDate)
    private val oneWeekEarlierDate = DateUtils.generate7daysPriorDate(date = todayDate)
    private val daysInThe7daysPrior =
        DateUtils.datesBetween(startDate = oneWeekEarlierDate, endDate = todayDate)

    private fun filtered(filter: String): Flow<List<TransEntity>> {
        return when (filter) {
            ConstantsChart.TODAY -> {
                transRepository.allTransactionsByDate(date = todayDate)
            }

            ConstantsChart.YESTERDAY -> {
                transRepository.allTransactionsByDate(date = yesterdayDate)
            }

            ConstantsChart.THIS_WEEK -> {
                transRepository.allTransactionsByBetweenDates(dates = weekDates)
            }

            ConstantsChart.LAST_7_DAYS -> {
                transRepository.allTransactionsByBetweenDates(dates = daysInThe7daysPrior)
            }

            ConstantsChart.THIS_MONTH -> {
                transRepository.allTransactionsByBetweenDates(dates = monthDates)
            }

            ConstantsChart.ALL -> {
                transRepository.allTransactions()
            }

            else -> {
                flow { emptyList<TransEntity>() }
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
        //loadData(dates = weekDates)
        data(filter = ConstantsChart.THIS_WEEK)
        Log.v(Constants.LOG_TAG, "Week days: $weekDates")
    }
}