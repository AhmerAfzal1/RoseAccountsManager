package com.ahmer.accounts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Constants

@Composable
fun ReportScreen(viewModel: MainViewModel) {
    val mState: MainState by viewModel.uiState.collectAsStateWithLifecycle()
    val mBalance: TransSumModel = mState.accountsBalance
    val mCategories: List<String> =
        listOf(Constants.TYPE_CREDIT_SUFFIX, Constants.TYPE_DEBIT_SUFFIX)
    val mColors: List<Color> = listOf(colorGreenDark, colorRedDark)
    val mValues: List<Double> = listOf(mBalance.creditSum, mBalance.debitSum)

    PieChart(values = mValues, colors = mColors, categories = mCategories)
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    values: List<Double>,
    colors: List<Color>,
    categories: List<String>,
) {
    var mStartAngle = 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Row(modifier = Modifier, horizontalArrangement = Arrangement.Center) {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(height = 250.dp)
                    .padding(all = 16.dp)
            ) {
                val mCanvasHeight: Float = size.height
                val mCanvasWidth: Float = size.width
                val mRadius: Float = minOf(a = mCanvasWidth, b = mCanvasHeight) / 2f

                values.forEachIndexed { index, amount ->
                    val mSweepAngles: Float = (amount.toFloat() / values.sum().toFloat()) * 360.0f
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = mStartAngle,
                        sweepAngle = mSweepAngles,
                        useCenter = false,
                        topLeft = Offset(
                            x = mCanvasWidth / 2f - mRadius, y = mCanvasHeight / 2f - mRadius
                        ),
                        size = Size(width = mRadius * 2f, height = mRadius * 2f),
                        style = Stroke(width = 130f, cap = StrokeCap.Butt),
                    )
                    mStartAngle += mSweepAngles
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            categories.forEachIndexed { index, label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(width = 30.dp)
                            .height(height = 30.dp)
                            .background(color = colors[index])
                    )
                    Text(
                        text = label.uppercase(),
                        modifier = Modifier.padding(all = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(height = 4.dp))
            }
        }
    }
}