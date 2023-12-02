package com.ahmer.accounts.ui

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.TabItem.Companion.Icons
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.chart.PieChart
import com.ahmer.accounts.utils.chart.PieChartData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: MainViewModel) {
    val mSurfaceColor: Color =
        if (MaterialTheme.colorScheme.isLight()) Color.Black else Color.Yellow
    val mSurfaceElevation: Dp = 4.dp

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier.shadow(
                elevation = mSurfaceElevation,
                ambientColor = mSurfaceColor,
                spotColor = mSurfaceColor,
            )
        ) {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.label_report))
            })
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val mState: MainState by viewModel.uiState.collectAsStateWithLifecycle()

            Tabs(mainState = mState)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tabs(mainState: MainState) {
    val mContext: Context = LocalContext.current
    val mList: List<TabItem> = TabItem.tabItems
    val mPagerState: PagerState = rememberPagerState(pageCount = { mList.size })
    var mSelectedTab: Int by rememberSaveable { mutableIntStateOf(value = 0) }

    LaunchedEffect(key1 = mSelectedTab) {
        mPagerState.animateScrollToPage(page = mSelectedTab)
    }

    LaunchedEffect(key1 = mPagerState.currentPage) {
        mSelectedTab = mPagerState.currentPage
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = mSelectedTab,
            modifier = Modifier,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(currentTabPosition = tabPositions[mSelectedTab]),
                    height = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            divider = { Divider(thickness = 3.dp) },
        ) {
            mList.forEachIndexed { index, tabItem ->
                val mSelected: Boolean = mSelectedTab == index
                Tab(
                    selected = mSelected,
                    onClick = { mSelectedTab = index },
                    modifier = Modifier,
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
                    icon = { tabItem.Icons(selected = mSelected) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                )
            }
        }

        HorizontalPager(
            state = mPagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f),
            key = { mList[it].title },
        ) { page ->
            when (page) {
                TabsScreen.CHART_BAR -> {}
                TabsScreen.CHART_PIE -> ChartScreen(mainState = mainState)
                else -> HelperUtils.showToast(
                    context = mContext,
                    msg = stringResource(id = R.string.toast_tab_requested, page)
                )
            }
        }
    }
}

@Composable
private fun ChartScreen(mainState: MainState) {
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            modifier = Modifier.size(size = 250.dp),
            inputs = mData,
            centerText = stringResource(id = R.string.label_balance).uppercase(),
        )
    }
}

private object TabsScreen {
    const val CHART_BAR = 0
    const val CHART_PIE = 1
    //const val CHART_ACCOUNTS = 2 //Later to implement, how many new accounts added daily, weekly and monthly
}

sealed class TabItem(
    @StringRes val title: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val contentDescription: Int,
) {
    data object Transactions : TabItem(
        title = R.string.label_trans,
        selectedIcon = R.drawable.ic_bar_chart,
        unselectedIcon = R.drawable.ic_bar_chart,
        contentDescription = R.string.content_description_bar,
    )

    data object Balance : TabItem(
        title = R.string.label_balance,
        selectedIcon = R.drawable.ic_filled_pie_chart,
        unselectedIcon = R.drawable.ic_outlined_pie_chart,
        contentDescription = R.string.content_description_pie,
    )

    companion object {
        val tabItems: List<TabItem> by lazy { listOf(Transactions, Balance) }

        @Composable
        fun TabItem.Icons(selected: Boolean) {
            Icon(
                painter = painterResource(id = if (selected) selectedIcon else unselectedIcon),
                contentDescription = stringResource(id = contentDescription),
            )
        }
    }
}