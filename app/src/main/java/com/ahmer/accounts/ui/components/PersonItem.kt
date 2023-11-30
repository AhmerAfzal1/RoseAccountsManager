package com.ahmer.accounts.ui.components

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.PersonsBalanceModel
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun PersonItem(
    personsBalanceModel: PersonsBalanceModel,
    currency: Currency,
    onEvent: (PersonEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val mBalance: Double = personsBalanceModel.balanceModel.balance
    val mContext: Context = LocalContext.current
    val mPersonsEntity: PersonsEntity = personsBalanceModel.personsEntity

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 6.dp, bottom = 6.dp)
            .clickable { onEvent(PersonEvent.OnAddTransactionClick(mPersonsEntity)) },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size = 40.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mPersonsEntity.name.first().toString(),
                modifier = Modifier.align(alignment = Alignment.Center),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.width(width = 8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(weight = 0.6f)) {
                Text(
                    text = mPersonsEntity.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall
                )
                if (mPersonsEntity.phone.isNotEmpty()) {
                    Text(
                        text = mPersonsEntity.phone,
                        color = Color.Gray,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            HelperUtils.AmountWithSymbolText(
                modifier = Modifier.weight(weight = 0.4f),
                context = mContext,
                currency = currency,
                amount = mBalance,
                color = if (mBalance >= 0) colorGreenDark else colorRedDark,
                isBold = false
            )
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
    )
}