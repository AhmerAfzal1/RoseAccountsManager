package com.ahmer.accounts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mViewModel: AddEditViewModel = hiltViewModel()
    var mTextAddress: String by rememberSaveable { mutableStateOf("") }
    var mTextEmail: String by rememberSaveable { mutableStateOf("") }
    var mTextName: String by rememberSaveable { mutableStateOf("") }
    var mTextNotes: String by rememberSaveable { mutableStateOf("") }
    var mTextPhone: String by rememberSaveable { mutableStateOf("") }

    var mTitle = stringResource(R.string.title_add_user)
    val mData: UserModel? =
        navHostController.previousBackStackEntry?.savedStateHandle?.get<UserModel>(Constants.NAV_ADD_EDIT_KEY)

    if (mData != null) {
        mTitle = stringResource(R.string.title_edit_user)
        mTextAddress = mData.address.toString()
        mTextEmail = mData.email.toString()
        mTextName = mData.name.toString()
        mTextNotes = mData.notes.toString()
        mTextPhone = mData.phone.toString()
    }

    fun clear() {
        mKeyboardController?.hide()
        mFocusManager.clearFocus()
        mTextAddress = ""
        mTextEmail = ""
        mTextName = ""
        mTextNotes = ""
        mTextPhone = ""
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(
                text = mTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { navHostController.navigateUp() }) { BackIcon() }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ), scrollBehavior = mScrollBehavior
        )
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

                    ElevatedButton(
                        onClick = {
                            val mUserModel: UserModel = mData?.copy(
                                id = mData.id,
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
                                clear()
                                delay(500.milliseconds)
                                mViewModel.insertOrUpdateUser(userModel = mUserModel)
                                delay(500.milliseconds)
                            }
                            navHostController.navigateUp()
                        },
                        modifier = Modifier.padding(top = 10.dp),
                        enabled = mTextName.isNotEmpty(),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                    ) {
                        Text(text = stringResource(R.string.label_save))
                    }
                }
            }
        )
    }
}