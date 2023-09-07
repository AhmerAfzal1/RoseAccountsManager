package com.ahmer.accounts.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.TransModel
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.utils.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransAddEditAlertDialog(
    typeState: String,
    transModel: TransModel,
    onEvent: (TransAddEditEvent) -> Unit,
    onSaveClick: () -> Unit
) {
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mLenDes = 64
    val mOpenDialog = remember { mutableStateOf(true) }
    val mOptions = listOf("Credit", "Debit")

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }
    if (mOpenDialog.value) {
        AlertDialog(
            onDismissRequest = { mOpenDialog.value = false },
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(0.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            ) {
                OutlinedTextField(
                    value = transModel.date,
                    onValueChange = { text ->
                        onEvent(TransAddEditEvent.OnDateChange(text))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    label = { Text(stringResource(R.string.label_date)) },
                    placeholder = { Text(stringResource(R.string.label_date)) },
                    trailingIcon = {
                        if (transModel.date.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (transModel.date.isNotEmpty()) {
                                    onEvent(TransAddEditEvent.OnDateChange(""))
                                }
                            })
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                ) {
                    mOptions.forEach { text ->
                        val mType = buildAnnotatedString {
                            append(text)
                            if (text == "Credit") {
                                append(" (+)")
                            } else {
                                append(" (-)")
                            }
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 5.dp)
                                .background(
                                    color =
                                    if (text == typeState) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        Color.LightGray
                                    }
                                )
                                .clickable { onEvent(TransAddEditEvent.OnTypeChange(text)) }
                        ) {
                            Text(
                                text = mType.text.uppercase(),
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = Color.White,
                                fontWeight = if (text == typeState) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = transModel.amount.toString(),
                    onValueChange = { text ->
                        onEvent(TransAddEditEvent.OnAmountChange(text))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .focusRequester(mFocusRequester)
                        .onFocusChanged { focus ->
                            if (focus.isFocused) {
                                mKeyboardController?.show()
                            }
                        },
                    label = { Text(stringResource(R.string.label_amount)) },
                    placeholder = { Text(stringResource(R.string.label_amount)) },
                    trailingIcon = {
                        if (transModel.amount.toString().isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (transModel.amount.toString().isNotEmpty()) {
                                    onEvent(TransAddEditEvent.OnAmountChange(""))
                                }
                            })
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        mFocusManager.moveFocus(FocusDirection.Down)
                    })
                )
                OutlinedTextField(
                    value = transModel.description,
                    onValueChange = { text ->
                        if (text.length <= mLenDes) {
                            onEvent(TransAddEditEvent.OnDescriptionChange(text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    label = { Text(stringResource(R.string.label_description)) },
                    placeholder = { Text(stringResource(R.string.label_description)) },
                    trailingIcon = {
                        if (transModel.description.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (transModel.description.isNotEmpty()) {
                                    onEvent(TransAddEditEvent.OnDescriptionChange(""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${transModel.description.length} / $mLenDes",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { clear() }),
                )

                LazyRow(modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp, bottom = 5.dp), content = {
                    item {
                        TextButton(
                            onClick = {
                                mOpenDialog.value = false
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.label_cancel),
                                fontSize = 14.sp
                            )
                        }
                        TextButton(
                            onClick = {
                                mOpenDialog.value = false
                                onSaveClick()
                            },
                        ) { Text(text = stringResource(R.string.label_save), fontSize = 14.sp) }
                    }
                })
            }
        }
    }
}