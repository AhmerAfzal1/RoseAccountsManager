package com.ahmer.accounts.ui.components

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.TransactionEntity
import com.ahmer.accounts.ui.isLight
import com.ahmer.accounts.ui.theme.colorSelectionDark
import com.ahmer.accounts.ui.theme.colorSelectionLight
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.HelperUtils.AmountWithSymbolText


/**
 * Composable item representing a transaction with details and amount display.
 *
 * @param transaction Transaction data to display
 * @param currency Currency type for amount formatting
 * @param modifier Modifier for styling and layout
 * @param isSelected Whether the item is in selected state
 * @param onClick Callback for regular click interaction
 * @param onLongClick Callback for long press interaction
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemTrans(
    modifier: Modifier = Modifier,
    transaction: TransactionEntity,
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: (Boolean) -> Unit,
) {
    val context: Context = LocalContext.current
    val selectionColor: Color =
        if (MaterialTheme.colorScheme.isLight()) colorSelectionDark else colorSelectionLight

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(color = if (isSelected) selectionColor else Color.Transparent)
                .combinedClickable(
                    enabled = true,
                    onLongClick = { onLongClick(true) },
                    onClick = onClick
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransactionDetailsSection(
                modifier = Modifier.weight(weight = 0.44f),
                description = transaction.description,
                date = transaction.date
            )

            TransactionAmountSection(
                modifier = Modifier.weight(weight = 0.5f),
                transaction = transaction,
                currency = currency,
                context = context
            )
        }

        HelperUtils.ListDivider(thickness = 2.dp, alpha = 0.2f)
    }
}

/**
 * Displays transaction description and date details.
 */
@Composable
private fun TransactionDetailsSection(
    modifier: Modifier = Modifier,
    description: String,
    date: Long
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val displayDescription = description.ifEmpty {
            stringResource(R.string.label_no_description)
        }

        Text(
            text = displayDescription,
            color = if (description.isEmpty()) Color.LightGray else Color.Unspecified,
            fontStyle = if (description.isEmpty()) FontStyle.Italic else FontStyle.Normal,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = if (description.isEmpty()) {
                MaterialTheme.typography.labelSmall
            } else {
                MaterialTheme.typography.labelMedium
            }
        )

        Text(
            text = HelperUtils.getDateTime(
                time = date,
                pattern = Constants.PATTERN_TRANSACTION_ITEM
            ),
            color = Color.Gray,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

/**
 * Displays transaction amount with type-specific formatting.
 */
@Composable
private fun TransactionAmountSection(
    modifier: Modifier = Modifier,
    transaction: TransactionEntity,
    currency: Currency,
    context: Context
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (transaction.type == Constants.TYPE_DEBIT) {
            AmountWithSymbolText(
                modifier = Modifier.weight(weight = 0.25f),
                context = context,
                currency = currency,
                amount = transaction.amount.toDouble(),
                isBold = false,
                isExpense = false,
                type = transaction.type
            )
            Spacer(modifier = Modifier.weight(weight = 0.25f))
        } else {
            Spacer(modifier = Modifier.weight(weight = 0.25f))
            AmountWithSymbolText(
                modifier = Modifier.weight(weight = 0.25f),
                context = context,
                currency = currency,
                amount = transaction.amount.toDouble(),
                isBold = false,
                isExpense = false,
                type = transaction.type
            )
        }
    }
}