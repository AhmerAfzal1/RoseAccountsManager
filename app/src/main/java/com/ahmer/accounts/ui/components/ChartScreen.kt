package com.ahmer.accounts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.ConstantsChart
import com.ahmer.accounts.utils.chart.PieChart
import com.ahmer.accounts.utils.chart.PieChartData
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun BalanceChartScreen(mainState: MainState) {
    val mData: List<PieChartData> by lazy {
        listOf(
            PieChartData(
                color = colorGreenDark,
                value = mainState.accountsBalance.creditSum,
                description = Constants.TYPE_CREDIT,
            ), PieChartData(
                color = colorRedDark,
                value = mainState.accountsBalance.debitSum,
                description = Constants.TYPE_DEBIT
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PieChart(
            modifier = Modifier.size(size = 250.dp),
            inputs = mData,
            centerText = stringResource(id = R.string.label_balance).uppercase(),
        )
    }
}

@Composable
fun TransactionsChartScreen(
    barChartList: List<BarChartData.Bar>,
    activeFilter: String,
    onChangeActiveFilter: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            BarChart(
                barChartData = BarChartData(bars = barChartList),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 300.dp)
                    .padding(all = 8.dp),
                animation = simpleChartAnimation(),
                barDrawer = SimpleBarDrawer(),
                xAxisDrawer = SimpleXAxisDrawer(
                    axisLineThickness = 1.dp,
                    axisLineColor = MaterialTheme.colorScheme.primary
                ),
                yAxisDrawer = SimpleYAxisDrawer(
                    axisLineThickness = 1.dp,
                    axisLineColor = MaterialTheme.colorScheme.primary
                ),
                labelDrawer = SimpleValueDrawer(
                    drawLocation = SimpleValueDrawer.DrawLocation.XAxis,
                    labelTextColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChartGraphCard(
                    filterName = ConstantsChart.THIS_WEEK,
                    isActive = activeFilter == ConstantsChart.THIS_WEEK,
                    onClick = {
                        onChangeActiveFilter(it)
                    }
                )
                ChartGraphCard(
                    filterName = ConstantsChart.LAST_7_DAYS,
                    isActive = activeFilter == ConstantsChart.LAST_7_DAYS,
                    onClick = {
                        onChangeActiveFilter(it)
                    }
                )
                ChartGraphCard(
                    filterName = ConstantsChart.THIS_MONTH,
                    isActive = activeFilter == ConstantsChart.THIS_MONTH,
                    onClick = {
                        onChangeActiveFilter(it)
                    }
                )
                ChartGraphCard(
                    filterName = ConstantsChart.ALL,
                    isActive = activeFilter == ConstantsChart.ALL,
                    onClick = {
                        onChangeActiveFilter(it)
                    }
                )
            }
        }
    }
}