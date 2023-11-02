package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmer.accounts.R
import com.ahmer.accounts.utils.DeleteIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAlertDialog(
    nameAccount: String,
    onConfirmClick: () -> Unit
) {
    val mOpenDialog = remember { mutableStateOf(value = true) }
    val mAnnotatedMsg = buildAnnotatedString {
        append("Do you really want to delete this ")
        withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
            append(nameAccount)
        }
        append(
            " account? All transaction(s) associated with this account will also be " +
                    "permanently deleted and this action cannot be undone and you will " +
                    "be unable to recover any data."
        )
    }

    if (mOpenDialog.value) {
        AlertDialog(
            onDismissRequest = { mOpenDialog.value = false },
        ) {
            ElevatedCard(
                modifier = Modifier.padding(all = 10.dp),
                shape = RoundedCornerShape(size = 5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                DeleteIcon(
                    modifier = Modifier
                        .size(size = 48.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 5.dp),
                    tint = AlertDialogDefaults.iconContentColor
                )

                Text(
                    text = stringResource(id = R.string.label_confirm),
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = mAnnotatedMsg,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                        .align(alignment = Alignment.Start),
                    style = MaterialTheme.typography.titleSmall
                )

                LazyRow(modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(end = 10.dp, bottom = 5.dp), content = {
                    item {
                        TextButton(
                            onClick = { mOpenDialog.value = false },
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_cancel),
                                fontSize = 14.sp
                            )
                        }
                        TextButton(
                            onClick = {
                                mOpenDialog.value = false
                                onConfirmClick()
                            },
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_delete),
                                fontSize = 14.sp
                            )
                        }
                    }
                })
            }
        }
    }
}