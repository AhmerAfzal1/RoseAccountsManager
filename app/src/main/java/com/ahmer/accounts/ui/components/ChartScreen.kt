package com.ahmer.accounts.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.ConstantsChart
import com.ahmer.accounts.utils.chart.PieChart
import com.ahmer.accounts.utils.chart.PieChartData
import com.ahmer.accounts.utils.chart.bar.BarChart
import com.ahmer.accounts.utils.chart.bar.BarChartData
import com.ahmer.accounts.utils.chart.bar.renderer.bar.SimpleBarDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.label.SimpleValueDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.xaxis.SimpleXAxisDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.yaxis.SimpleYAxisDrawer
import com.ahmer.accounts.utils.chart.bar.simpleChartAnimation

private const val SERIAL_NUMBER_WEIGHT = 0.125f
private const val DATE_TYPE_WEIGHT = 0.3125f
private const val AMOUNT_WEIGHT = 0.25f

/**
 * Displays a pie chart showing the balance distribution between credit and debit.
 *
 * @param mainState The state containing account balance information
 */
@Composable
fun BalanceChartScreen(mainState: MainState) {
    val chartData = remember(mainState) {
        listOf(
            PieChartData(
                color = colorGreenDark,
                value = mainState.accountsBalance.creditSum,
                description = Constants.TYPE_CREDIT,
            ),
            PieChartData(
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
            inputs = chartData,
            centerText = stringResource(id = R.string.label_balance).uppercase(),
        )
    }
}

/**
 * Main container for transactions chart screen
 *
 * @param barChartData List of data points for the bar chart
 * @param activeFilter Currently selected time filter
 * @param onActiveFilterChanged Callback when filter changes
 * @param transactions List of transactions to display
 */
@Composable
fun TransactionsChartScreen(
    barChartData: List<BarChartData.Bar>,
    activeFilter: String,
    onActiveFilterChanged: (String) -> Unit,
    transactions: List<TransactionEntity>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        BarChartItem(barChartList = barChartData)
        TimeFilterSelector(activeFilter = activeFilter, onFilterSelected = onActiveFilterChanged)
        TransactionHeader()
        TransactionList(transactions = transactions)
    }
}

/**
 * Displays a bar chart with given data
 *
 * @param barChartList List of bars to display in the chart
 * @param modifier Modifier for layout customization
 */
@Composable
fun BarChartItem(modifier: Modifier = Modifier, barChartList: List<BarChartData.Bar>) {
    BarChart(
        barChartData = BarChartData(bars = barChartList),
        modifier = modifier
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
            axisLineColor = MaterialTheme.colorScheme.primary,
            labelTextColor = MaterialTheme.colorScheme.primary
        ),
        labelDrawer = SimpleValueDrawer(
            drawLocation = SimpleValueDrawer.DrawLocation.XAxis,
            labelTextColor = MaterialTheme.colorScheme.primary
        )
    )
}

/**
 * Horizontal selector for time filters
 *
 * @param activeFilter Currently active filter
 * @param onFilterSelected Callback when filter is selected
 * @param modifier Modifier for layout customization
 */
@Composable
private fun TimeFilterSelector(
    activeFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters: List<String> = listOf(
        ConstantsChart.THIS_WEEK,
        ConstantsChart.LAST_7_DAYS,
        ConstantsChart.THIS_MONTH,
        ConstantsChart.ALL
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(state = rememberScrollState())
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        filters.forEach { filter ->
            ChartFilterCard(
                filterName = filter,
                isActive = activeFilter == filter,
                onClick = onFilterSelected
            )
        }
    }
}

@Composable
private fun RowScope.HeaderText(textRes: Int, weight: Float) {
    Text(
        text = stringResource(textRes).uppercase(),
        modifier = Modifier.weight(weight = weight),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold
    )
}

/**
 * Header row for transactions list
 *
 * @param modifier Modifier for layout customization
 */
@Composable
fun TransactionHeader(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            HeaderText(
                textRes = R.string.label_sr_no,
                weight = SERIAL_NUMBER_WEIGHT
            )
            HeaderText(
                textRes = R.string.label_created_on,
                weight = DATE_TYPE_WEIGHT
            )
            HeaderText(
                textRes = R.string.label_type,
                weight = DATE_TYPE_WEIGHT
            )
            HeaderText(
                textRes = R.string.label_by_amount,
                weight = AMOUNT_WEIGHT
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        thickness = 2.dp,
        color = Color.LightGray.copy(alpha = 0.2f)
    )
}

/**
 * Scrollable list of transactions
 *
 * @param transactions List of transactions to display
 * @param modifier Modifier for layout customization
 */
@Composable
fun TransactionList(
    transactions: List<TransactionEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        itemsIndexed(
            items = transactions,
            key = { _, transaction -> transaction.id }) { index, transaction ->
            TransactionRow(index = index, transaction = transaction)
        }
    }
}

/**
 * Single row representing a transaction
 * @param index Position in the list
 * @param transaction Transaction data to display
 * @param modifier Modifier for layout customization
 */
@Composable
private fun TransactionRow(
    index: Int,
    transaction: TransactionEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = (index + 1).toString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(SERIAL_NUMBER_WEIGHT)
        )
        Text(
            text = transaction.createdOn,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(DATE_TYPE_WEIGHT)
        )
        Text(
            text = transaction.type,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(DATE_TYPE_WEIGHT)
        )
        Text(
            text = transaction.amount,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(AMOUNT_WEIGHT)
        )
    }
}