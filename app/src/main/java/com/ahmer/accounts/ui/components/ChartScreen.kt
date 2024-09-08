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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.TransEntity
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
    tabTransactions: List<TransEntity>,
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
                    onClick = { onChangeActiveFilter(it) }
                )
                ChartGraphCard(
                    filterName = ConstantsChart.LAST_7_DAYS,
                    isActive = activeFilter == ConstantsChart.LAST_7_DAYS,
                    onClick = { onChangeActiveFilter(it) }
                )
                ChartGraphCard(
                    filterName = ConstantsChart.THIS_MONTH,
                    isActive = activeFilter == ConstantsChart.THIS_MONTH,
                    onClick = { onChangeActiveFilter(it) }
                )
                ChartGraphCard(
                    filterName = ConstantsChart.ALL,
                    isActive = activeFilter == ConstantsChart.ALL,
                    onClick = { onChangeActiveFilter(it) }
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = CardDefaults.shape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = stringResource(R.string.label_sr_no).uppercase(),
                        modifier = Modifier.weight(weight = 1f),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.label_created_on).uppercase(),
                        modifier = Modifier.weight(weight = 1f),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.label_type).uppercase(),
                        modifier = Modifier.weight(weight = 1f),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.label_by_amount).uppercase(),
                        modifier = Modifier.weight(weight = 1f),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
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

        items(items = tabTransactions, key = { tranId -> tranId.id }) { transaction ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = (tabTransactions.indexOf(transaction) + 1).toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(weight = 1f)
                )
                Text(
                    text = transaction.createdOn,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(weight = 1f)
                )
                Text(
                    text = transaction.type,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(weight = 1f)
                )
                Text(
                    text = transaction.amount,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(weight = 1f)
                )
            }
        }
    }
}