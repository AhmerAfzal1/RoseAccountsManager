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

/**
 * A customizable card component for chart filter selection.
 *
 * @param modifier Modifier to be applied to the container Box
 * @param filterName The display text for the filter option
 * @param isActive Whether this filter is currently selected
 * @param onClick Callback when the filter is selected, returns the filterName
 */
@Composable
fun ChartFilterCard(
    modifier: Modifier = Modifier, filterName: String, isActive: Boolean, onClick: (String) -> Unit
) {
    val color = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .padding(all = 4.dp)
            .clickable { onClick(filterName) }) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .thenIf(isActive) {
                    border(
                        width = 1.dp, color = color, shape = RoundedCornerShape(size = 4.dp)
                    )
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = filterName, style = TextStyle(
                    color = color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

/**
 * Helper extension function to conditionally apply modifiers.
 *
 * @param condition The condition to check
 * @param modifierBlock Lambda returning the modifier to apply if condition is true
 */
private fun Modifier.thenIf(
    condition: Boolean, modifierBlock: Modifier.() -> Modifier
): Modifier = this.then(
    if (condition) modifierBlock() else Modifier
)