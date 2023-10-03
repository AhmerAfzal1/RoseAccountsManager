package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.dialogs.DeleteAlertDialog
import com.ahmer.accounts.dialogs.MoreInfoAlertDialog
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.EditIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.InfoIcon

@Composable
fun PersonItem(
    personsEntity: PersonsEntity,
    onEvent: (PersonEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val mPadding: Dp = 5.dp
    var mShowDeleteDialog by remember { mutableStateOf(value = false) }
    var mShowInfoDialog by remember { mutableStateOf(value = false) }

    if (mShowDeleteDialog) {
        DeleteAlertDialog(
            nameAccount = personsEntity.name,
            onConfirmClick = { onEvent(PersonEvent.OnDeleteClick(personsEntity)) }
        )
    }

    if (mShowInfoDialog) {
        MoreInfoAlertDialog(personsEntity)
    }

    ElevatedCard(
        modifier = modifier.clickable { onEvent(PersonEvent.OnAddTransactionClick(personsEntity)) },
        shape = RoundedCornerShape(size = 10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = personsEntity.name,
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
                    onClick = { onEvent(PersonEvent.OnEditClick(personsEntity)) },
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
            val mTotalCredit = 0.0
            val mTotalDebit = 0.0
            val mTotalBalance = HelperUtils.getRoundedValue((mTotalCredit.minus(mTotalDebit)))

            val mText: String = if (personsEntity.phone.isEmpty()) {
                "Balance: $mTotalBalance"
            } else {
                "Phone: ${personsEntity.phone}  |  Balance: $mTotalBalance"
            }
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = mText,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}