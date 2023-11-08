package com.ahmer.accounts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.PersonsBalanceModel
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.ui.theme.colorGreenDark
import com.ahmer.accounts.ui.theme.colorRedDark
import com.ahmer.accounts.utils.HelperUtils

@Composable
fun PersonItem(
    personsBalanceModel: PersonsBalanceModel,
    onEvent: (PersonEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val mBalance: String = personsBalanceModel.balanceModel.balance ?: "0.0"
    val mPersonsEntity: PersonsEntity = personsBalanceModel.personsEntity
    /*var mShowDeleteDialog: Boolean by remember { mutableStateOf(value = false) }
    var mShowInfoDialog: Boolean by remember { mutableStateOf(value = false) }
    val mPadding: Dp = 5.dp

    if (mShowDeleteDialog) {
        DeleteAlertDialog(
            nameAccount = mPersonsEntity.name,
            onConfirmClick = { onEvent(PersonEvent.OnDeleteClick(mPersonsEntity)) }
        )
    }

    if (mShowInfoDialog) {
        MoreInfoAlertDialog(mPersonsEntity)
    }*/

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .clickable { onEvent(PersonEvent.OnAddTransactionClick(mPersonsEntity)) },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size = 48.dp)
                .border(
                    border = BorderStroke(width = 2.dp, color = Color.LightGray),
                    shape = CircleShape
                )
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mPersonsEntity.name.first().toString(),
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.width(width = 8.dp))
        Column {
            Row {
                Text(
                    text = mPersonsEntity.name,
                    modifier = Modifier.weight(weight = 0.65f),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Rs. ${HelperUtils.getRoundedValue(value = mBalance.toDouble())}",
                    modifier = Modifier.weight(weight = 0.35f),
                    color = if (mBalance.toDouble() >= 0) colorGreenDark else colorRedDark,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (mPersonsEntity.phone.isNotEmpty()) {
                Text(
                    text = mPersonsEntity.phone,
                    color = Color.Gray,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
    )

    /*ElevatedCard(
        modifier = modifier.clickable { onEvent(PersonEvent.OnAddTransactionClick(mPersonsEntity)) },
        shape = RoundedCornerShape(size = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mPersonsEntity.name,
                modifier = Modifier.padding(start = mPadding),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { mShowInfoDialog = true },
                    modifier = Modifier.then(Modifier.size(size = Constants.ICON_SIZE)),
                ) { InfoIcon() }
                IconButton(
                    onClick = { onEvent(PersonEvent.OnEditClick(mPersonsEntity)) },
                    modifier = Modifier.then(Modifier.size(size = Constants.ICON_SIZE)),
                ) { EditIcon() }
                IconButton(
                    onClick = { mShowDeleteDialog = true },
                    modifier = Modifier.then(Modifier.size(size = Constants.ICON_SIZE)),
                ) { DeleteIcon() }
            }
        }
        Column(
            modifier = Modifier.padding(end = mPadding, bottom = mPadding)
        ) {
            val mText: String = if (mPersonsEntity.phone.isEmpty()) {
                "Balance: ${mBalanceModel.balance}"
            } else {
                "Phone: ${mPersonsEntity.phone}  |  Balance: ${mBalanceModel.balance}"
            }
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = mText,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }*/
}