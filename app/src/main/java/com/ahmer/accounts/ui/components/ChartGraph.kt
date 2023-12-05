package com.ahmer.accounts.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChartGraphCard(
    modifier: Modifier = Modifier,
    filterName: String,
    isActive: Boolean,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(all = 8.dp)
            .clickable { onClick(filterName) }
    ) {
        if (isActive) {
            Row(
                modifier = modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(size = 4.dp),
                    )
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = filterName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                )
            }
        } else {
            Row(
                modifier = modifier
                    .padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = filterName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}