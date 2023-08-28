package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.HelperFunctions
import com.ahmer.accounts.utils.InfoIcon

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

@OptIn(ExperimentalMaterial3Api::class)
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
        "Modified:",
    )
    val mOpenDialog = remember { mutableStateOf(true) }

    if (mOpenDialog.value) {
        AlertDialog(
            onDismissRequest = { mOpenDialog.value = false }) {
            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            ) {
                InfoIcon(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterHorizontally),
                    tint = AlertDialogDefaults.iconContentColor
                )

                LazyColumn(
                    modifier = Modifier.padding(
                        start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp
                    )
                ) {
                    itemsIndexed(mTitleList) { index, title ->
                        Row {
                            InfoText(text = title, weight = 1f, isTitle = true)
                            InfoText(text = mDataList[index], weight = 3f, isTitle = false)
                        }
                    }
                }

                TextButton(
                    onClick = { mOpenDialog.value = false },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp, bottom = 5.dp),
                ) {
                    Text(text = stringResource(R.string.label_ok), fontSize = 14.sp)
                }
            }
        }
    }
}