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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditScreen(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val mViewModel: AddEditViewModel = hiltViewModel()
    var mTextName by rememberSaveable { mutableStateOf("") }
    var mTextAddress by rememberSaveable { mutableStateOf("") }
    var mTextPhone by rememberSaveable { mutableStateOf("") }
    var mTextEmail by rememberSaveable { mutableStateOf("") }
    var mTextNotes by rememberSaveable { mutableStateOf("") }
    val mKeyboardController = LocalSoftwareKeyboardController.current
    val mFocusManager = LocalFocusManager.current
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    fun clear() {
        mKeyboardController?.hide()
        mFocusManager.clearFocus()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.title_add_user),
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
                        onValueChange = { mTextName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.label_name)) },
                        placeholder = { Text(stringResource(R.string.label_name)) },
                        trailingIcon = {
                            if (mTextName.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextName.isNotEmpty()) {
                                        mTextName = ""
                                    }
                                })
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = mTextAddress,
                        onValueChange = { mTextAddress = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_address)) },
                        placeholder = { Text(stringResource(R.string.label_address)) },
                        trailingIcon = {
                            if (mTextAddress.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextAddress.isNotEmpty()) {
                                        mTextAddress = ""
                                    }
                                })
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            mFocusManager.moveFocus(FocusDirection.Down)
                        })
                    )

                    OutlinedTextField(
                        value = mTextPhone,
                        onValueChange = { mTextPhone = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_phone_number)) },
                        placeholder = { Text(stringResource(R.string.label_phone_number)) },
                        trailingIcon = {
                            if (mTextPhone.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextPhone.isNotEmpty()) {
                                        mTextPhone = ""
                                    }
                                })
                            }
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
                        onValueChange = { mTextEmail = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_email)) },
                        placeholder = { Text(stringResource(R.string.label_email)) },
                        trailingIcon = {
                            if (mTextEmail.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextEmail.isNotEmpty()) {
                                        mTextEmail = ""
                                    }
                                })
                            }
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
                        onValueChange = { mTextNotes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        label = { Text(stringResource(R.string.label_notes)) },
                        placeholder = { Text(stringResource(R.string.label_notes)) },
                        trailingIcon = {
                            if (mTextNotes.isNotEmpty()) {
                                CloseIcon(modifier = Modifier.clickable {
                                    if (mTextNotes.isNotEmpty()) {
                                        mTextNotes = ""
                                    }
                                })
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { clear() }),
                        minLines = 4
                    )

                    ElevatedButton(
                        onClick = {
                            clear()
                            val mUserModel = UserModel(
                                name = mTextName,
                                address = mTextAddress,
                                email = mTextEmail,
                                phone = mTextPhone,
                                notes = mTextNotes,
                            )
                            mViewModel.insertOrUpdateUser(userModel = mUserModel)
                            navHostController.navigateUp()
                        },
                        modifier = Modifier.padding(top = 10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                    ) {
                        Text("Save")
                    }

                }
            })
    }
}