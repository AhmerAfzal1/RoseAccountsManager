package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.model.CategoryModel
import com.ahmer.accounts.event.ExpenseEvent

@Composable
fun ItemExpense(
    modifier: Modifier = Modifier,
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
        val mTitle: String = expenseEntity.category
        Icon(
            painter = painterResource(id = CategoryModel.getIconByTitle(title = mTitle)),
            contentDescription = "$mTitle icon"
        )

        Spacer(modifier = Modifier.width(width = 16.dp))

        Column {
            Text(text = mTitle)
            Text(text = expenseEntity.description)
        }

        Spacer(modifier = Modifier.weight(weight = 1f))
        Text(
            text = expenseEntity.amount,
            modifier = modifier.padding(end = 16.dp)
        )
    }
}