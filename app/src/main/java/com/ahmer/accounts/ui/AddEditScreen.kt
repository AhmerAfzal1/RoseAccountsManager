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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.SaveIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditScreen(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mLenAddress = 128
    val mLenEmail = 64
    val mLenName = 64
    val mLenNotes = 512
    val mLenPhone = 15
    val mNavUserData: UserModel? =
        navHostController.previousBackStackEntry?.savedStateHandle?.get<UserModel>(Constants.NAV_ADD_EDIT_KEY)
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mShowSnackBar: MutableState<Boolean> = remember { mutableStateOf(false) }
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mSnackBarMessage: String = stringResource(R.string.title_snack_bar_text_field)
    val mViewModel: AddEditViewModel = hiltViewModel()
    var mTextAddress: String by rememberSaveable { mutableStateOf("") }
    var mTextEmail: String by rememberSaveable { mutableStateOf("") }
    var mTextName: String by rememberSaveable { mutableStateOf("") }
    var mTextNotes: String by rememberSaveable { mutableStateOf("") }
    var mTextPhone: String by rememberSaveable { mutableStateOf("") }
    var mTitle: String = stringResource(R.string.title_add_user)

    if (mNavUserData != null) {
        mTextAddress = mNavUserData.address.toString()
        mTextEmail = mNavUserData.email.toString()
        mTextName = mNavUserData.name.toString()
        mTextNotes = mNavUserData.notes.toString()
        mTextPhone = mNavUserData.phone.toString()
        mTitle = stringResource(R.string.title_edit_user)
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
        mTextAddress = ""
        mTextEmail = ""
        mTextName = ""
        mTextNotes = ""
        mTextPhone = ""
    }

    fun save() {
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
    }

    LaunchedEffect(mShowSnackBar.value) {
        if (mShowSnackBar.value) {
            mSnackBarHostState.showSnackbar(mSnackBarMessage)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = mTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }, navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) { BackIcon() }
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
                if (mTextName.isEmpty()) mShowSnackBar.value = !mShowSnackBar.value else save()
            }) { SaveIcon() }
        }) { innerPadding ->
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
            contentPadding = innerPadding,
            content = {
                item {
                    OutlinedTextField(
                        value = mTextName,
                        onValueChange = { if (it.length <= mLenName) mTextName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.label_name)) },
                        placeholder = { Text(stringResource(R.string.label_name)) },
                        trailingIcon = {
                            if (mTextName.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextName.isNotEmpty()) mTextName = ""
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mTextName.length} / $mLenName",
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
                        value = mTextAddress,
                        onValueChange = { if (it.length <= mLenAddress) mTextAddress = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_address)) },
                        placeholder = { Text(stringResource(R.string.label_address)) },
                        trailingIcon = {
                            if (mTextAddress.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextAddress.isNotEmpty()) mTextAddress = ""
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mTextAddress.length} / $mLenAddress",
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
                        value = mTextPhone,
                        onValueChange = { if (it.length <= mLenPhone) mTextPhone = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_phone_number)) },
                        placeholder = { Text(stringResource(R.string.label_phone_number)) },
                        trailingIcon = {
                            if (mTextPhone.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextPhone.isNotEmpty()) mTextPhone = ""
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mTextPhone.length} / $mLenPhone",
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
                        value = mTextEmail,
                        onValueChange = { if (it.length <= mLenEmail) mTextEmail = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_email)) },
                        placeholder = { Text(stringResource(R.string.label_email)) },
                        trailingIcon = {
                            if (mTextEmail.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextEmail.isNotEmpty()) mTextEmail = ""
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mTextEmail.length} / $mLenEmail",
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
                        value = mTextNotes,
                        onValueChange = { if (it.length <= mLenNotes) mTextNotes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_notes)) },
                        placeholder = { Text(stringResource(R.string.label_notes)) },
                        trailingIcon = {
                            if (mTextNotes.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextNotes.isNotEmpty()) mTextNotes = ""
                                })
                            }
                        },
                        supportingText = {
                            Text(
                                text = "${mTextNotes.length} / $mLenNotes",
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