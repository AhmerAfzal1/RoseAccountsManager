package com.ahmer.accounts.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.state.ReportState
import com.ahmer.accounts.ui.components.BalanceChartScreen
import com.ahmer.accounts.ui.components.TransactionsChartScreen
import com.ahmer.accounts.utils.ConstantsChart
import com.ahmer.accounts.utils.DateUtils
import com.ahmer.accounts.utils.chart.bar.BarChartData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: ReportViewModel) {
    val isLightTheme: Boolean = MaterialTheme.colorScheme.isLight()
    val surfaceColor: Color = if (isLightTheme) Color.Black else Color.Yellow

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier.shadow(
                elevation = 8.dp,
                ambientColor = surfaceColor,
                spotColor = surfaceColor,
            )
        ) {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.label_report))
            })
        }
    }) { innerPadding ->
        val state: ReportState by viewModel.state.collectAsStateWithLifecycle()
        val activeFilter: String by viewModel.activeFilter.collectAsStateWithLifecycle()
        val transactions: List<TransactionEntity> = state.transactions
        val tabs: List<ReportTabItem> = ReportTabItem.entries
        val pagerState: PagerState = rememberPagerState(pageCount = { tabs.size })
        var selectedTab: Int by rememberSaveable { mutableIntStateOf(value = 0) }
        val color: Color = MaterialTheme.colorScheme.primary

        LaunchedEffect(selectedTab) {
            pagerState.animateScrollToPage(selectedTab)
        }

        LaunchedEffect(pagerState.currentPage) {
            selectedTab = pagerState.currentPage
        }

        val barChartData: List<BarChartData.Bar> = remember(transactions, activeFilter) {
            when {
                activeFilter == ConstantsChart.THIS_WEEK || activeFilter == ConstantsChart.LAST_7_DAYS -> {
                    transactions.groupBy { it.createdOn }.map { (date, items) ->
                        BarChartData.Bar(
                            value = items.sumOf { it.amount.toDouble() }.toFloat(),
                            label = DateUtils.actualDayOfWeek(date).take(3),
                            color = color
                        )
                    }
                }

                transactions.isEmpty() -> {
                    listOf(
                        BarChartData.Bar(
                            value = 0.toFloat(), label = "", color = color
                        )
                    )
                }

                else -> {
                    transactions.mapIndexed { index, item ->
                        BarChartData.Bar(
                            value = item.amount.toFloat(), label = "${index + 1}", color = color
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ReportTabRow(
                tabs = tabs, selectedTab = selectedTab, onTabSelected = { selectedTab = it })

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxSize()
                    .weight(weight = 1f)
            ) { page ->
                when (tabs[page]) {
                    is ReportTabItem.Transactions -> TransactionsChartScreen(
                        barChartData = barChartData,
                        activeFilter = activeFilter,
                        onFilterChanged = viewModel::updateActiveFilter,
                        transactions = state.transactions
                    )

                    is ReportTabItem.Balance -> BalanceChartScreen(
                        transactionSumModel = state.transactionSum
                    )
                }
            }
        }
    }
}

@Composable
fun ReportTabRow(
    modifier: Modifier = Modifier,
    tabs: List<ReportTabItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(selectedTabIndex = selectedTab, modifier = modifier, indicator = { tabPositions ->
        SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            height = 3.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }, divider = { HorizontalDivider(thickness = 3.dp) }) {
        tabs.forEachIndexed { index, tabItem ->
            val isSelected: Boolean = selectedTab == index

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                enabled = true,
                text = {
                    Text(
                        text = stringResource(id = tabItem.title),
                        modifier = Modifier.padding(all = 16.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (isSelected) tabItem.selectedIcon else tabItem.unselectedIcon
                        ), contentDescription = stringResource(id = tabItem.contentDescription)
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            )
        }
    }
}

sealed class ReportTabItem(
    @StringRes val title: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val contentDescription: Int,
) {
    data object Transactions : ReportTabItem(
        title = R.string.label_trans,
        selectedIcon = R.drawable.ic_bar_chart,
        unselectedIcon = R.drawable.ic_bar_chart,
        contentDescription = R.string.content_bar,
    )

    data object Balance : ReportTabItem(
        title = R.string.label_balance,
        selectedIcon = R.drawable.ic_filled_pie_chart,
        unselectedIcon = R.drawable.ic_outlined_pie_chart,
        contentDescription = R.string.content_pie,
    )

    companion object {
        val entries = listOf(Transactions, Balance)
    }
}