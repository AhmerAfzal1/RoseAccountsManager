package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.HelperFunctions

@Composable
fun RowScope.InfoText(text: String, weight: Float, isTitle: Boolean = false) {
    if (isTitle) {
        Text(
            modifier = Modifier
                .weight(weight)
                .padding(top = 3.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    } else {
        Text(
            modifier = Modifier
                .weight(weight)
                .padding(start = 2.dp, top = 3.dp),
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun MoreInfoAlertDialog(userModel: UserModel) {
    val mDataList = listOf(
        "${userModel.name}",
        "${userModel.address}",
        "${userModel.phone}",
        "${userModel.email}",
        "${userModel.notes}",
        HelperFunctions.getDateTime(userModel.created),
        HelperFunctions.getDateTime(userModel.modified),
    )
    val mTitleList = listOf(
        "Name:",
        "Address:",
        "Phone:",
        "Email:",
        "Notes:",
        "Created:",
        "LastModified:",
    )
    val mOpenDialog = remember { mutableStateOf(true) }

    if (mOpenDialog.value) {
        AlertDialog(
            onDismissRequest = { mOpenDialog.value = false },
            icon = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(id = R.string.content_description_info)
                )
            },
            tonalElevation = AlertDialogDefaults.TonalElevation,
            text = {
                LazyColumn {
                    itemsIndexed(mTitleList) { index, title ->
                        Row {
                            InfoText(text = title, weight = 1f, isTitle = true)
                            InfoText(text = mDataList[index], weight = 2f, isTitle = false)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mOpenDialog.value = false }) { Text("Ok") }
            },
        )
    }
}