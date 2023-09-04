package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.event.AddEditEvent
import com.ahmer.accounts.utils.CloseIcon

@Composable
fun AddEditTextFields(
    modifier: Modifier = Modifier,
    userModel: UserModel?,
    onEvent: (AddEditEvent) -> Unit
) {
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }

    val mLenAddress = 128
    val mLenEmail = 64
    val mLenName = 64
    val mLenNotes = 512
    val mLenPhone = 15

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            content = {
                item {
                    OutlinedTextField(
                        value = userModel?.name ?: "",
                        onValueChange = { text ->
                            if (text.length <= mLenName) {
                                onEvent(AddEditEvent.OnNameChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(mFocusRequester)
                            .onFocusChanged { focus ->
                                if (focus.isFocused) {
                                    mKeyboardController?.show()
                                }
                            },
                        label = { Text(stringResource(R.string.label_name)) },
                        placeholder = { Text(stringResource(R.string.label_name)) },
                        trailingIcon = {
                            if (userModel?.name!!.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (userModel.name.isNotEmpty()) {
                                        onEvent(AddEditEvent.OnNameChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${userModel?.name?.length} / $mLenName",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = userModel?.address ?: "",
                        onValueChange = { text ->
                            if (text.length <= mLenAddress) {
                                onEvent(AddEditEvent.OnAddressChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_address)) },
                        placeholder = { Text(stringResource(R.string.label_address)) },
                        trailingIcon = {
                            if (userModel?.address!!.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (userModel.address.isNotEmpty()) {
                                        onEvent(AddEditEvent.OnAddressChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${userModel?.address?.length} / $mLenAddress",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = userModel?.phone ?: "",
                        onValueChange = { text ->
                            if (text.length <= mLenPhone) {
                                onEvent(AddEditEvent.OnPhoneChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_phone_number)) },
                        placeholder = { Text(stringResource(R.string.label_phone_number)) },
                        trailingIcon = {
                            if (userModel?.phone!!.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (userModel.phone.isNotEmpty()) {
                                        onEvent(AddEditEvent.OnPhoneChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${userModel?.phone?.length} / $mLenPhone",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = userModel?.email ?: "",
                        onValueChange = { text ->
                            if (text.length <= mLenEmail) {
                                onEvent(AddEditEvent.OnEmailChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_email)) },
                        placeholder = { Text(stringResource(R.string.label_email)) },
                        trailingIcon = {
                            if (userModel?.email!!.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (userModel.email.isNotEmpty()) {
                                        onEvent(AddEditEvent.OnEmailChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${userModel?.email?.length} / $mLenEmail",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = userModel?.notes ?: "",
                        onValueChange = { text ->
                            if (text.length <= mLenNotes) {
                                onEvent(AddEditEvent.OnNotesChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_notes)) },
                        placeholder = { Text(stringResource(R.string.label_notes)) },
                        trailingIcon = {
                            if (userModel?.notes!!.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (userModel.notes.isNotEmpty()) {
                                        onEvent(AddEditEvent.OnNotesChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${userModel?.notes?.length} / $mLenNotes",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { clear() }),
                        minLines = 4
                    )
                }
            }
        )
    }
}