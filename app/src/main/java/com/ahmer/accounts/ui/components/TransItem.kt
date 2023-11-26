package com.ahmer.accounts.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransEntity
import com.ahmer.accounts.ui.isLight
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorSelectionDark
import com.ahmer.accounts.ui.theme.colorSelectionLight
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.HelperUtils.AmountWithSymbolText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransItem(
    transEntity: TransEntity,
    currency: Currency,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: (Boolean) -> Unit,
) {
    val mSelectionColor: Color =
        if (MaterialTheme.colorScheme.isLight()) colorSelectionLight else colorSelectionDark

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
            .background(color = if (isSelected) mSelectionColor else Color.Transparent)
            .combinedClickable(
                enabled = true,
                onLongClick = { onLongClick(true) },
                onClick = { onClick() },
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.44f),
        ) {
            val mDesc: String = transEntity.description
            Text(
                text = mDesc.ifEmpty { stringResource(R.string.label_no_description) },
                color = if (mDesc.isEmpty()) Color.LightGray else Color.Unspecified,
                fontStyle = if (mDesc.isEmpty()) FontStyle.Italic else FontStyle.Normal,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = if (mDesc.isEmpty()) {
                    MaterialTheme.typography.labelSmall
                } else {
                    MaterialTheme.typography.labelMedium
                }
            )
            Text(
                text = HelperUtils.getDateTime(
                    time = transEntity.date, pattern = Constants.PATTERN_TRANSACTION_ITEM
                ),
                color = Color.Gray,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 2.dp)
                .weight(weight = 0.5f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (transEntity.type == "Debit") {
                AmountWithSymbolText(
                    modifier = Modifier.weight(weight = 0.25f),
                    currency = currency,
                    amount = transEntity.amount.toDouble(),
                    color = colorRedDark,
                    isBold = false,
                )
                Text(text = "", modifier = Modifier.weight(weight = 0.25f))
            } else {
                Text(text = "", modifier = Modifier.weight(weight = 0.25f))
                AmountWithSymbolText(
                    modifier = Modifier.weight(weight = 0.25f),
                    currency = currency,
                    amount = transEntity.amount.toDouble(),
                    color = colorGreenDark,
                    isBold = false,
                )
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        thickness = 2.dp,
        color = Color.LightGray.copy(alpha = 0.2f)
    )
}