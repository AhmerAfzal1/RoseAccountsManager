package com.ahmer.accounts.utils.chart.bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.utils.chart.bar.BarChartUtils.axisAreas
import com.ahmer.accounts.utils.chart.bar.BarChartUtils.barDrawableArea
import com.ahmer.accounts.utils.chart.bar.BarChartUtils.forEachWithArea
import com.ahmer.accounts.utils.chart.bar.renderer.bar.BarDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.bar.SimpleBarDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.label.LabelDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.label.SimpleValueDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.xaxis.SimpleXAxisDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.xaxis.XAxisDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.yaxis.SimpleYAxisDrawer
import com.ahmer.accounts.utils.chart.bar.renderer.yaxis.YAxisDrawer

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPx.toPx() }
}

@Composable
fun BarChart(
    barChartData: BarChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    barDrawer: BarDrawer = SimpleBarDrawer(),
    xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = SimpleYAxisDrawer(),
    labelDrawer: LabelDrawer = SimpleValueDrawer(),
    barWidth: Float = 40f, // Width of each bar in dp
    barSpacing: Float = 16f // Spacing between bars in dp
) {
    val transitionAnimation = remember(barChartData.bars) { Animatable(initialValue = 0f) }

    LaunchedEffect(barChartData.bars) {
        transitionAnimation.animateTo(1f, animationSpec = animation)
    }

    val progress = transitionAnimation.value
    val scrollState = rememberScrollState()
    val totalBarsWidth = (barChartData.bars.size * (barWidth + barSpacing))
    val chartWidth = totalBarsWidth + barSpacing

    Canvas(
        modifier = modifier
            .horizontalScroll(scrollState)
            .fillMaxHeight()
            .width(chartWidth.dp) // Ensures bars occupy full width
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val (xAxisArea, yAxisArea) = axisAreas(
                        drawScope = this,
                        totalSize = size,
                        xAxisDrawer = xAxisDrawer,
                        labelDrawer = labelDrawer
                    )
                    val barDrawableArea = barDrawableArea(xAxisArea)

                    // Draw yAxis line.
                    yAxisDrawer.drawAxisLine(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = yAxisArea
                    )

                    // Draw xAxis line.
                    xAxisDrawer.drawAxisLine(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = xAxisArea
                    )

                    // Draw each bar.
                    barChartData.forEachWithArea(
                        this,
                        barDrawableArea,
                        progress,
                        labelDrawer
                    ) { barArea, bar ->
                        barDrawer.drawBar(
                            drawScope = this,
                            canvas = canvas,
                            barArea = barArea,
                            bar = bar
                        )
                    }
                }
            }
    ) {
        drawIntoCanvas { canvas ->
            val (xAxisArea, yAxisArea) = axisAreas(
                drawScope = this,
                totalSize = size,
                xAxisDrawer = xAxisDrawer,
                labelDrawer = labelDrawer
            )
            val barDrawableArea = barDrawableArea(xAxisArea)

            barChartData.forEachWithArea(
                this,
                barDrawableArea,
                progress,
                labelDrawer
            ) { barArea, bar ->
                labelDrawer.drawLabel(
                    drawScope = this,
                    canvas = canvas,
                    label = bar.label,
                    barArea = barArea,
                    xAxisArea = xAxisArea
                )
            }

            yAxisDrawer.drawAxisLabels(
                drawScope = this,
                canvas = canvas,
                minValue = barChartData.minYValue,
                maxValue = barChartData.maxYValue,
                drawableArea = yAxisArea
            )
        }
    }
}