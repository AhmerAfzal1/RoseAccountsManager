package com.ahmer.accounts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ahmer.accounts.utils.DateIcon

@Composable
fun TransAddEditTextFields(
    transModel: TransModel,
    onEvent: (TransAddEditEvent) -> Unit,
    titleButton: String
) {
    val mDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mLenDes = 64
    val mOptions = listOf("Credit", "Debit")

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    if (mDatePickerDialog.value) {
        TransDatePickDialog(onEvent)
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 15.dp)
    ) {
        OutlinedTextField(value = transModel.date,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { mDatePickerDialog.value = it.isFocused },
            readOnly = true,
            label = { Text(stringResource(R.string.label_date)) },
            placeholder = { Text(stringResource(R.string.label_date)) },
            trailingIcon = {
                DateIcon(modifier = Modifier.clickable {
                    mDatePickerDialog.value = true
                })
            })
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
        ) {
            mOptions.forEach { text ->
                val mType = buildAnnotatedString {
                    append(text)
                    if (text == "Credit") append(" (+)") else append(" (-)")
                }
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)
                        .background(
                            color = if (text == transModel.type) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.LightGray
                            }
                        )
                        .clickable { onEvent(TransAddEditEvent.OnTypeChange(text)) }) {
                    Text(
                        text = mType.text.uppercase(),
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color.White,
                        fontWeight = if (text == transModel.type) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        OutlinedTextField(
            value = transModel.amount,
            onValueChange = { text ->
                onEvent(TransAddEditEvent.OnAmountChange(text))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .focusRequester(mFocusRequester)
                .onFocusChanged { focus ->
                    if (focus.isFocused) {
                        mKeyboardController?.show()
                    }
                },
            label = { Text(stringResource(R.string.label_amount)) },
            placeholder = { Text(stringResource(R.string.label_amount)) },
            trailingIcon = {
                if (transModel.amount.isNotEmpty()) {
                    CloseIcon(modifier = Modifier.clickable {
                        if (transModel.amount.isNotEmpty()) {
                            onEvent(TransAddEditEvent.OnAmountChange(""))
                        }
                    })
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
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
                .padding(top = 15.dp),
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
                capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { clear() }),
            minLines = 2
        )

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 20.dp)
        ) {
            Button(onClick = { onEvent(TransAddEditEvent.OnSaveClick) }) {
                Text(text = titleButton, fontSize = 14.sp)
            }
        }
    }
}