package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.state.MainState
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

@Composable
private fun Tabs(mainState: MainState) {
    val mContext: Context = LocalContext.current
    var mTab: Int by remember { mutableIntStateOf(value = 0) }
    val mTitles: List<String> = listOf(
        stringResource(id = R.string.label_trans), stringResource(id = R.string.label_balance),
    )

    TabRow(selectedTabIndex = mTab) {
        mTitles.forEachIndexed { index, title ->
            val mSelected: Boolean = mTab == index
            Tab(
                selected = mSelected,
                onClick = { mTab = index },
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    when (mTab) {
        TabsScreen.CHART_BAR -> {}
        TabsScreen.CHART_PIE -> ChartScreen(mainState = mainState)
        else -> HelperUtils.showToast(
            context = mContext, msg = stringResource(id = R.string.toast_tab_requested, mTab)
        )
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
}