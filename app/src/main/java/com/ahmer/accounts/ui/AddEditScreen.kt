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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddOrEditScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val mShowSnackBar: MutableState<Boolean> = remember { mutableStateOf(false) }
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                UiEvent.PopBackStack -> onPopBackStack()
                else -> Unit
            }
        }
    }

    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mLenAddress = 128
    val mLenEmail = 64
    val mLenName = 64
    val mLenNotes = 512
    val mLenPhone = 15
    //val mNavUserData: UserModel? =
    //    navHostController.previousBackStackEntry?.savedStateHandle?.get<UserModel>(Constants.NAV_ADD_EDIT_KEY)
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    //val mShowSnackBar: MutableState<Boolean> = remember { mutableStateOf(false) }
    //val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mSnackBarMessage: String = stringResource(R.string.title_snack_bar_text_field)
    val mViewModel: AddEditViewModel = hiltViewModel()
    /*var mTextAddress: String by rememberSaveable { mutableStateOf("") }
    var mTextEmail: String by rememberSaveable { mutableStateOf("") }
    var mTextName: String by rememberSaveable { mutableStateOf("") }
    var mTextNotes: String by rememberSaveable { mutableStateOf("") }
    var mTextPhone: String by rememberSaveable { mutableStateOf("") }*/
    var mTitle: String = stringResource(R.string.title_add_user)

    /*if (mNavUserData != null) {
        mTextAddress = mNavUserData.address.toString()
        mTextEmail = mNavUserData.email.toString()
        mTextName = mNavUserData.name.toString()
        mTextNotes = mNavUserData.notes.toString()
        mTextPhone = mNavUserData.phone.toString()
        mTitle = stringResource(R.string.title_edit_user)
    }*/

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    /*fun save() {
        val mUserModel: UserModel = mNavUserData?.copy(
            id = mNavUserData.id,
            name = mTextName,
            address = mTextAddress,
            email = mTextEmail,
            phone = mTextPhone,
            notes = mTextNotes,
            modified = System.currentTimeMillis()
        ) ?: UserModel(
            name = mTextName,
            address = mTextAddress,
            email = mTextEmail,
            phone = mTextPhone,
            notes = mTextNotes,
        )
        mCoroutineScope.launch {
            mViewModel.insertOrUpdateUser(userModel = mUserModel)
            clear()
            delay(250.milliseconds)
        }
        navHostController.navigateUp()
    }*/

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = mTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ), scrollBehavior = mScrollBehavior
            )
        }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditEvent.OnSaveClick)
                /*if (mTextName.isEmpty()) mShowSnackBar.value = !mShowSnackBar.value else save()*/
            }) { SaveIcon() }
        }) { innerPadding ->
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            contentPadding = innerPadding,
            content = {
                item {
                    OutlinedTextField(
                        value = viewModel.name,
                        onValueChange = {
                            if (it.length <= mLenName) {
                                viewModel.onEvent(AddEditEvent.OnNameChange(it))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.label_name)) },
                        placeholder = { Text(stringResource(R.string.label_name)) },
                        trailingIcon = {
                            if (viewModel.name.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (viewModel.name.isNotEmpty()) {
                                        viewModel.onEvent(AddEditEvent.OnNameChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${viewModel.name.length} / $mLenName",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = viewModel.address,
                        onValueChange = {
                            if (it.length <= mLenAddress) {
                                viewModel.onEvent(AddEditEvent.OnAddressChange(it))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_address)) },
                        placeholder = { Text(stringResource(R.string.label_address)) },
                        trailingIcon = {
                            if (viewModel.address.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (viewModel.address.isNotEmpty()) {
                                        viewModel.onEvent(AddEditEvent.OnAddressChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${viewModel.address.length} / $mLenAddress",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = viewModel.phone,
                        onValueChange = {
                            if (it.length <= mLenPhone) {
                                viewModel.onEvent(AddEditEvent.OnPhoneChange(it))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_phone_number)) },
                        placeholder = { Text(stringResource(R.string.label_phone_number)) },
                        trailingIcon = {
                            if (viewModel.phone.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (viewModel.phone.isNotEmpty()) {
                                        viewModel.onEvent(AddEditEvent.OnPhoneChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${viewModel.phone.length} / $mLenPhone",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = {
                            if (it.length <= mLenEmail) {
                                viewModel.onEvent(AddEditEvent.OnEmailChange(it))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_email)) },
                        placeholder = { Text(stringResource(R.string.label_email)) },
                        trailingIcon = {
                            if (viewModel.email.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (viewModel.email.isNotEmpty()) {
                                        viewModel.onEvent(AddEditEvent.OnEmailChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${viewModel.email.length} / $mLenEmail",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = viewModel.notes,
                        onValueChange = {
                            if (it.length <= mLenNotes) {
                                viewModel.onEvent(AddEditEvent.OnNotesChange(it))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_notes)) },
                        placeholder = { Text(stringResource(R.string.label_notes)) },
                        trailingIcon = {
                            if (viewModel.notes.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (viewModel.notes.isNotEmpty()) {
                                        viewModel.onEvent(AddEditEvent.OnNotesChange(""))
                                    }
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${viewModel.notes.length} / $mLenNotes",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { clear() }),
                        minLines = 4
                    )
                }
            }
        )
    }
}