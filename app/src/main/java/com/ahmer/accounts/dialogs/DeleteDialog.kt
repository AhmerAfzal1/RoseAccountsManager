package com.ahmer.accounts.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.ahmer.accounts.R
import com.ahmer.accounts.utils.DeleteIcon

@Composable
fun DeleteAlertDialog(
    modifier: Modifier = Modifier,
    accountName: String,
    onConfirmClick: () -> Unit
) {
    val mAnnotatedMsg: AnnotatedString = buildAnnotatedString {
        append(text = "Do you really want to delete this ")
        withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
            append(text = accountName)
        }
        append(
            text = " account? All transaction(s) associated with this account will also be " +
                    "permanently deleted and this action cannot be undone and you will " +
                    "be unable to recover any data."
        )
    }
    var mOpenDialog: Boolean by remember { mutableStateOf(value = true) }

    if (mOpenDialog) {
        AlertDialog(
            onDismissRequest = { mOpenDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        mOpenDialog = false
                        onConfirmClick()
                    },
                ) {
                    Text(text = stringResource(id = R.string.label_delete))
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = { mOpenDialog = false }) {
                    Text(text = stringResource(id = R.string.label_cancel))
                }
            },
            icon = { DeleteIcon() },
            title = { Text(text = stringResource(id = R.string.label_confirm)) },
            text = { Text(text = mAnnotatedMsg) }
        )
    }
}