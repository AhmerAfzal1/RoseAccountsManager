package com.ahmer.accounts.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.model.CategoryModel
import com.ahmer.accounts.event.ExpenseEvent
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun ItemExpense(
    modifier: Modifier = Modifier,
    currency: Currency,
    expenseEntity: ExpenseEntity,
    onEvent: (ExpenseEvent) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onEvent(ExpenseEvent.OnEdit(expenseEntity = expenseEntity)) },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val mCategory: String = expenseEntity.category
        val mContext: Context = LocalContext.current
        val mDesc: String = expenseEntity.description
        Icon(
            painter = painterResource(id = CategoryModel.getIconByTitle(title = mCategory)),
            contentDescription = "$mCategory icon",
            modifier = Modifier.size(size = 32.dp)
        )

        Spacer(modifier = Modifier.width(width = 16.dp))

        Column {
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
                text = mCategory,
                color = Color.DarkGray,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.weight(weight = 1f))
        HelperUtils.AmountWithSymbolText(
            modifier = modifier.padding(end = 16.dp),
            context = mContext,
            currency = currency,
            amount = expenseEntity.amount.toDouble(),
            isBold = false,
            isExpense = true,
            type = expenseEntity.type,
        )
    }
}