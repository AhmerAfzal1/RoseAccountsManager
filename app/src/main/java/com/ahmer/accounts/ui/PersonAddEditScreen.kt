package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.event.PersonAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.utils.AddressIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.EmailIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MyTextField
import com.ahmer.accounts.utils.NotesIcon
import com.ahmer.accounts.utils.PersonIcon
import com.ahmer.accounts.utils.PhoneIcon
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonAddEditScreen(viewModel: PersonAddEditViewModel, onPopBackStack: () -> Unit) {
    val isLightTheme: Boolean = MaterialTheme.colorScheme.isLight()
    val mContext: Context = LocalContext.current
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mSurfaceColor: Color = if (isLightTheme) Color.Black else Color.Yellow
    val mSurfaceElevation: Dp = 4.dp

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                else -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier.shadow(
                elevation = mSurfaceElevation,
                ambientColor = mSurfaceColor,
                spotColor = mSurfaceColor,
            )
        ) {
            TopAppBar(
                title = { Text(text = viewModel.titleBar) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            clear()
                            onPopBackStack()
                        },
                        modifier = Modifier.size(size = Constants.ICON_SIZE)
                    ) { BackIcon() }
                },
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            PersonAddEditMain(
                viewModel = viewModel,
                focusManager = mFocusManager,
                focusRequester = mFocusRequester,
                keyboardController = mKeyboardController,
            )
        }
    }
}

@Composable
fun PersonAddEditMain(
    viewModel: PersonAddEditViewModel,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {
    var isMoreData: Boolean by remember { mutableStateOf(value = false) }
    val mState by viewModel.uiState.collectAsState()

    fun clear() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }
    mState.person?.let { personsEntity ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyTextField(
                value = personsEntity.name,
                onValueChange = {
                    if (it.length <= Constants.LEN_NAME) {
                        viewModel.onEvent(PersonAddEditEvent.OnNameChange(it))
                    }
                },
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            keyboardController?.show()
                        }
                    },
                label = { Text(stringResource(id = R.string.label_name)) },
                leadingIcon = { PersonIcon() },
                trailingIcon = {
                    if (personsEntity.name.isNotEmpty()) {
                        CloseIcon(modifier = Modifier.clickable {
                            if (personsEntity.name.isNotEmpty()) {
                                viewModel.onEvent(PersonAddEditEvent.OnNameChange(name = ""))
                            }
                        })
                    }
                },
                supportingText = {
                    Text(
                        text = "${personsEntity.name.length} / ${Constants.LEN_NAME}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )

            MyTextField(
                value = personsEntity.phone,
                onValueChange = {
                    if (it.length <= Constants.LEN_PHONE) {
                        viewModel.onEvent(PersonAddEditEvent.OnPhoneChange(it))
                    }
                },
                label = { Text(stringResource(id = R.string.label_phone_number)) },
                leadingIcon = { PhoneIcon() },
                trailingIcon = {
                    if (personsEntity.phone.isNotEmpty()) {
                        CloseIcon(modifier = Modifier.clickable {
                            if (personsEntity.phone.isNotEmpty()) {
                                viewModel.onEvent(PersonAddEditEvent.OnPhoneChange(phone = ""))
                            }
                        })
                    }
                },
                supportingText = {
                    Text(
                        text = "${personsEntity.phone.length} / ${Constants.LEN_PHONE}",
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
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )

            if (!isMoreData) {
                OutlinedButton(onClick = { isMoreData = true }) {
                    Text(text = stringResource(R.string.label_add_more_data))
                }
            }

            if (isMoreData) {
                MyTextField(
                    value = personsEntity.email,
                    onValueChange = {
                        if (it.length <= Constants.LEN_EMAIL) {
                            viewModel.onEvent(PersonAddEditEvent.OnEmailChange(it))
                        }
                    },
                    label = { Text(stringResource(id = R.string.label_email)) },
                    leadingIcon = { EmailIcon() },
                    trailingIcon = {
                        if (personsEntity.email.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.email.isNotEmpty()) {
                                    viewModel.onEvent(PersonAddEditEvent.OnEmailChange(email = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity.email.length} / ${Constants.LEN_EMAIL}",
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
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                MyTextField(
                    value = personsEntity.address,
                    onValueChange = {
                        if (it.length <= Constants.LEN_ADDRESS) {
                            viewModel.onEvent(PersonAddEditEvent.OnAddressChange(it))
                        }
                    },
                    label = { Text(stringResource(id = R.string.label_address)) },
                    leadingIcon = { AddressIcon() },
                    trailingIcon = {
                        if (personsEntity.address.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.address.isNotEmpty()) {
                                    viewModel.onEvent(
                                        PersonAddEditEvent.OnAddressChange(address = "")
                                    )
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity.address.length} / ${Constants.LEN_ADDRESS}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                MyTextField(
                    value = personsEntity.notes,
                    onValueChange = {
                        if (it.length <= Constants.LEN_NOTES) {
                            viewModel.onEvent(PersonAddEditEvent.OnNotesChange(it))
                        }
                    },
                    label = { Text(stringResource(id = R.string.label_notes)) },
                    leadingIcon = { NotesIcon() },
                    trailingIcon = {
                        if (personsEntity.notes.isNotEmpty()) {
                            CloseIcon(modifier = Modifier.clickable {
                                if (personsEntity.notes.isNotEmpty()) {
                                    viewModel.onEvent(PersonAddEditEvent.OnNotesChange(notes = ""))
                                }
                            })
                        }
                    },
                    supportingText = {
                        Text(
                            text = "${personsEntity.notes.length} / ${Constants.LEN_NOTES}",
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
            }

            OutlinedButton(
                onClick = {
                    clear()
                    viewModel.onEvent(PersonAddEditEvent.OnSaveClick)
                },
                enabled = personsEntity.name.isNotEmpty()
            ) {
                Text(text = stringResource(id = R.string.label_save).uppercase())
            }
        }
    }
}