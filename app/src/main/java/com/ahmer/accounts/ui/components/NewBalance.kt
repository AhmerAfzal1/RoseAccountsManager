package com.ahmer.accounts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.TrendingDownIcon
import com.ahmer.accounts.utils.TrendingUpIcon

@Composable
fun ItemBalanceV1(
    balanceData: BalanceData,
    person: PersonEntity? = null,
    onAction: (BalanceAction) -> Unit = {}
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF6366F1), Color(0xFF9333EA))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = gradientBrush,
                    alpha = 0.9f
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                person?.let {
                    GlassPersonSection(it, onAction)
                }

                // Credit/Debit Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlassBalanceCard(
                        modifier = Modifier.weight(1f),
                        amount = balanceData.transactionSum.creditSum,
                        label = "${stringResource(R.string.label_total)} ${Constants.TYPE_CREDIT}",
                        icon = {
                            TrendingUpIcon(
                                modifier = Modifier.size(size = 24.dp),
                                tint = Color(0xFF4ADE80)
                            )
                        },
                        currency = balanceData.currency,
                    )

                    GlassBalanceCard(
                        modifier = Modifier.weight(1f),
                        amount = balanceData.transactionSum.debitSum,
                        label = "${stringResource(R.string.label_total)} ${Constants.TYPE_DEBIT}",
                        icon = {
                            TrendingDownIcon(
                                modifier = Modifier.size(size = 24.dp),
                                tint = Color(0xFFF87171)
                            )
                        },
                        currency = balanceData.currency,
                    )
                }

                GlassBalanceCard(
                    modifier = Modifier.fillMaxWidth(),
                    amount = balanceData.transactionSum.balance,
                    label = stringResource(R.string.label_total_balance),
                    icon = {
                        if (balanceData.transactionSum.balance >= 0) {
                            TrendingUpIcon(
                                modifier = Modifier.size(size = 24.dp),
                                tint = Color(0xFF60A5FA)
                            )
                        } else {
                            TrendingDownIcon(
                                modifier = Modifier.size(size = 24.dp),
                                tint = Color(0xFFF87171)
                            )
                        }
                    },
                    currency = balanceData.currency,
                )
            }
        }
    }
}

@Composable
private fun GlassPersonSection(
    person: PersonEntity,
    onAction: (BalanceAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = person.name,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (person.phone.isNotEmpty()) {
                Text(
                    text = person.phone,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                )
            }
        }

        Row {
            IconButton(
                onClick = { onAction(BalanceAction.Delete) },
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.2f)
                )
            ) {
                DeleteIcon(tint = Color.White)
            }
            // Repeat for other actions...
        }
    }
}

@Composable
private fun GlassBalanceCard(
    modifier: Modifier = Modifier,
    amount: Double,
    label: String,
    icon: @Composable () -> Unit,
    currency: Currency,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon()

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
                Text(
                    text = "${currency.symbol} ${HelperUtils.roundValue(amount)}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}