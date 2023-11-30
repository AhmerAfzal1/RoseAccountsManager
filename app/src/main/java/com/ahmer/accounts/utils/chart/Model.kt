package com.ahmer.accounts.utils.chart

import androidx.compose.ui.graphics.Color

data class PieChartData(
    val color: Color,
    val value: Double,
    val description: String
)

data class PieChartValue(
    val text: String,
    val x: Float,
    val y: Float,
    val rotate: Float
)