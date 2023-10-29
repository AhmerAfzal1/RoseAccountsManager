package com.ahmer.accounts.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.event.PersonAddEditEvent
import com.ahmer.accounts.utils.CloseIcon

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PersonAddEditTextFields(
    personsEntity: PersonsEntity?,
    onEvent: (PersonAddEditEvent) -> Unit
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

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(all = 16.dp),
        content = {
            item {
                OutlinedTextField(
                    value = personsEntity?.name ?: "",
                    onValueChange = { text ->
                        if (text.length <= mLenName) {
                            onEvent(PersonAddEditEvent.OnNameChange(text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester = mFocusRequester)
                        .onFocusChanged { focus ->
                            if (focus.isFocused) {
                                mKeyboardController?.show()
                            }
                        },
                    label = { Text(stringResource(id = R.string.label_name)) },
                    placeholder = { Text(stringResource(id = R.string.label_name)) },
                    trailingIcon = {
                        if (personsEntity?.name!!.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.name.isNotEmpty()) {
                                    onEvent(PersonAddEditEvent.OnNameChange(name = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity?.name?.length} / $mLenName",
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
                    value = personsEntity?.address ?: "",
                    onValueChange = { text ->
                        if (text.length <= mLenAddress) {
                            onEvent(PersonAddEditEvent.OnAddressChange(text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    label = { Text(stringResource(id = R.string.label_address)) },
                    placeholder = { Text(stringResource(id = R.string.label_address)) },
                    trailingIcon = {
                        if (personsEntity?.address!!.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.address.isNotEmpty()) {
                                    onEvent(PersonAddEditEvent.OnAddressChange(address = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity?.address?.length} / $mLenAddress",
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
                    value = personsEntity?.phone ?: "",
                    onValueChange = { text ->
                        if (text.length <= mLenPhone) {
                            onEvent(PersonAddEditEvent.OnPhoneChange(text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    label = { Text(stringResource(id = R.string.label_phone_number)) },
                    placeholder = { Text(stringResource(id = R.string.label_phone_number)) },
                    trailingIcon = {
                        if (personsEntity?.phone!!.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.phone.isNotEmpty()) {
                                    onEvent(PersonAddEditEvent.OnPhoneChange(phone = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity?.phone?.length} / $mLenPhone",
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
                    value = personsEntity?.email ?: "",
                    onValueChange = { text ->
                        if (text.length <= mLenEmail) {
                            onEvent(PersonAddEditEvent.OnEmailChange(text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    label = { Text(stringResource(id = R.string.label_email)) },
                    placeholder = { Text(stringResource(id = R.string.label_email)) },
                    trailingIcon = {
                        if (personsEntity?.email!!.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.email.isNotEmpty()) {
                                    onEvent(PersonAddEditEvent.OnEmailChange(email = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity?.email?.length} / $mLenEmail",
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
                    value = personsEntity?.notes ?: "",
                    onValueChange = { text ->
                        if (text.length <= mLenNotes) {
                            onEvent(PersonAddEditEvent.OnNotesChange(text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    label = { Text(stringResource(id = R.string.label_notes)) },
                    placeholder = { Text(stringResource(id = R.string.label_notes)) },
                    trailingIcon = {
                        if (personsEntity?.notes!!.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.notes.isNotEmpty()) {
                                    onEvent(PersonAddEditEvent.OnNotesChange(notes = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity?.notes?.length} / $mLenNotes",
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