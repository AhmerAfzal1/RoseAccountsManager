package com.ahmer.accounts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.event.AddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.SaveIcon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddOrEditScreen(
    onPopBackStack: () -> Unit, modifier: Modifier = Modifier
) {
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: AddEditViewModel = hiltViewModel()

    val mLenAddress = 128
    val mLenEmail = 64
    val mLenName = 64
    val mLenNotes = 512
    val mLenPhone = 15

    LaunchedEffect(key1 = true) {
        mViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                }

                UiEvent.PopBackStack -> onPopBackStack()
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

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = mViewModel.titleBar, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }, navigationIcon = {
            IconButton(onClick = { onPopBackStack() }) { BackIcon() }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ), scrollBehavior = mScrollBehavior
        )
    }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) }, floatingActionButton = {
        FloatingActionButton(onClick = {
            mViewModel.onEvent(AddEditEvent.OnSaveClick)
        }) { SaveIcon() }
    }) { innerPadding ->
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            contentPadding = innerPadding,
            content = {
                item {
                    OutlinedTextField(
                        value = mViewModel.name,
                        onValueChange = { text ->
                            if (text.length <= mLenName) {
                                mViewModel.onEvent(AddEditEvent.OnNameChange(text))
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
                            if (mViewModel.name.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mViewModel.name.isNotEmpty()) {
                                        mViewModel.onEvent(AddEditEvent.OnNameChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mViewModel.name.length} / $mLenName",
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
                        value = mViewModel.address,
                        onValueChange = { text ->
                            if (text.length <= mLenAddress) {
                                mViewModel.onEvent(AddEditEvent.OnAddressChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_address)) },
                        placeholder = { Text(stringResource(R.string.label_address)) },
                        trailingIcon = {
                            if (mViewModel.address.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mViewModel.address.isNotEmpty()) {
                                        mViewModel.onEvent(AddEditEvent.OnAddressChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mViewModel.address.length} / $mLenAddress",
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
                        value = mViewModel.phone,
                        onValueChange = { text ->
                            if (text.length <= mLenPhone) {
                                mViewModel.onEvent(AddEditEvent.OnPhoneChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_phone_number)) },
                        placeholder = { Text(stringResource(R.string.label_phone_number)) },
                        trailingIcon = {
                            if (mViewModel.phone.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mViewModel.phone.isNotEmpty()) {
                                        mViewModel.onEvent(AddEditEvent.OnPhoneChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mViewModel.phone.length} / $mLenPhone",
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
                        value = mViewModel.email,
                        onValueChange = { text ->
                            if (text.length <= mLenEmail) {
                                mViewModel.onEvent(AddEditEvent.OnEmailChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_email)) },
                        placeholder = { Text(stringResource(R.string.label_email)) },
                        trailingIcon = {
                            if (mViewModel.email.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mViewModel.email.isNotEmpty()) {
                                        mViewModel.onEvent(AddEditEvent.OnEmailChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mViewModel.email.length} / $mLenEmail",
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
                        value = mViewModel.notes,
                        onValueChange = { text ->
                            if (text.length <= mLenNotes) {
                                mViewModel.onEvent(AddEditEvent.OnNotesChange(text))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_notes)) },
                        placeholder = { Text(stringResource(R.string.label_notes)) },
                        trailingIcon = {
                            if (mViewModel.notes.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mViewModel.notes.isNotEmpty()) {
                                        mViewModel.onEvent(AddEditEvent.OnNotesChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mViewModel.notes.length} / $mLenNotes",
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