package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.ui.theme.colorRedLight
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.CreditIcon
import com.ahmer.accounts.utils.DebitIcon
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.HelperFunctions

@Composable
fun TransItem(
    transModel: TransModel,
    onEvent: (TransEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(bottom = 3.dp)
            .fillMaxWidth()
            .clickable { onEvent(TransEvent.OnEditClick(transModel)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (transModel.type == "Debit") {
            DebitIcon(modifier = Modifier.size(Constants.ICON_SIZE), tint = colorRedDark)
        } else {
            CreditIcon(modifier = Modifier.size(Constants.ICON_SIZE), tint = colorGreenDark)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val mDescription: String = transModel.description
            Text(
                text = "Rs. ${HelperFunctions.getDecimalRoundedValue(transModel.amount.toDouble())}",
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium
            )
            if (mDescription.isNotEmpty()) {
                Text(
                    text = mDescription,
                    color = Color.DarkGray,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = transModel.date,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = { onEvent(TransEvent.OnDeleteClick(transModel)) }) {
            DeleteIcon(modifier = Modifier.size(Constants.ICON_SIZE))
        }
    }
    HorizontalDivider(thickness = 1.dp)
}