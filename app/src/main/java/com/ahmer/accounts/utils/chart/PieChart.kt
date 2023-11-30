package com.ahmer.accounts.utils.chart

import android.annotation.SuppressLint
import android.graphics.Paint
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmer.accounts.utils.HelperUtils

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PieChart(
    modifier: Modifier,
    inputs: List<PieChartData>? = null,
    colorBackground: Color = Color.White,
    titleCenterColor: Color = Color.Black,
    centerText: String,
    fontFamily: FontFamily = FontFamily.Default,
) {
    var list: List<PieChartData>? by remember { mutableStateOf(value = null) }
    var circleCenter by remember { mutableStateOf(value = Offset.Zero) }
    var parentInnerRadius by remember { mutableFloatStateOf(value = 0f) }
    var values: MutableList<PieChartValue> by remember { mutableStateOf(value = mutableListOf()) }

    LaunchedEffect(key1 = inputs) {
        list = inputs
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = modifier) {
                val width = size.width
                val height = size.height
                val innerRadius = width / 4f
                parentInnerRadius = innerRadius
                val radius = innerRadius * 2f
                val transparentWidth = innerRadius / 4f

                circleCenter = Offset(x = width / 2f, y = height / 2f)

                val totalValue = list?.sumOf { it.value } ?: 1.0
                val anglePerValue = 360f / totalValue
                var currentStartAngle = 0.0

                list?.forEachIndexed { index, pieChartInput ->
                    val scale = 1.0f
                    val angleToDraw = pieChartInput.value * anglePerValue
                    scale(scale) {
                        drawArc(
                            color = pieChartInput.color,
                            startAngle = currentStartAngle.toFloat(),
                            sweepAngle = angleToDraw.toFloat(),
                            useCenter = true,
                            size = Size(width = radius * 2f, height = radius * 2f),
                            topLeft = Offset(
                                x = (width - radius * 2f) / 2f, y = (height - radius * 2f) / 2f
                            )
                        )
                        currentStartAngle += angleToDraw
                    }
                    var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f
                    var factor = 1f
                    if (rotateAngle > 90f) {
                        rotateAngle = (rotateAngle + 180).mod(360f)
                        factor = -0.92f
                    }

                    val percentage = (pieChartInput.value / totalValue * 100.0)

                    if (index == 0) {
                        values = mutableListOf()
                    }

                    values.add(
                        PieChartValue(
                            text = "${HelperUtils.roundValue(value = percentage)} %",
                            x = circleCenter.x,
                            y = circleCenter.y + (radius - (radius - innerRadius) / 2f) * factor,
                            rotate = rotateAngle.toFloat()
                        )
                    )
                }

                values.forEach { value ->
                    drawContext.canvas.nativeCanvas.apply {
                        rotate(degrees = value.rotate) {
                            drawText(value.text, value.x, value.y, Paint().apply {
                                textSize = 16.sp.toPx()
                                textAlign = Paint.Align.CENTER
                                color = Color.White.toArgb()
                            })
                        }
                    }
                }

                drawContext.canvas.nativeCanvas.apply {
                    drawCircle(
                        circleCenter.x,
                        circleCenter.y,
                        innerRadius,
                        Paint().apply {
                            color = colorBackground.toArgb()
                            setShadowLayer(10f, 0f, 0f, Color.White.toArgb())
                        }
                    )
                }

                drawCircle(
                    color = colorBackground.copy(alpha = 0.25f),
                    radius = innerRadius + transparentWidth / 2f
                )
            }

            Text(
                text = centerText,
                modifier = Modifier
                    .width(width = Dp(value = parentInnerRadius / 1.5f))
                    .padding(all = 12.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                color = titleCenterColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            list?.forEach { pieChartInput ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(width = 30.dp)
                            .height(height = 30.dp)
                            .background(color = pieChartInput.color)
                    )
                    Text(
                        text = pieChartInput.description.uppercase(),
                        modifier = Modifier.padding(all = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(height = 4.dp))
            }
        }
    }
}