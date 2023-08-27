package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R

@Composable
fun DeleteAlertDialog(
    nameAccount: String,
    onConfirmClick: () -> Unit
) {
    val mOpenDialog = remember { mutableStateOf(true) }
    val mAnnotatedMsg = buildAnnotatedString {
        append("Do you really want to delete this ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(nameAccount) }
        append(
            " account? All transaction(s) associated with this account will also be " +
                    "permanently deleted and this action cannot be undone and you will " +
                    "be unable to recover any data."
        )
    }

    if (mOpenDialog.value) {
        AlertDialog(
            onDismissRequest = { mOpenDialog.value = false },
            icon = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = stringResource(id = R.string.content_description_delete)
                )
            },
            title = { Text(text = "Confirmation", fontWeight = FontWeight.Bold) },
            tonalElevation = AlertDialogDefaults.TonalElevation,
            text = { Text(text = mAnnotatedMsg) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmClick()
                        mOpenDialog.value = false
                    }
                ) { Text(text = "Delete") }
            },
            dismissButton = {
                TextButton(onClick = { mOpenDialog.value = false }
                ) { Text(text = "Cancel") }
            }
        )
    }
}