package com.ahmer.accounts.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.dialogs.DeleteAlertDialog
import com.ahmer.accounts.dialogs.MoreInfoAlertDialog
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.TransState
import com.ahmer.accounts.ui.components.PersonTotalBalance
import com.ahmer.accounts.ui.components.TransItem
import com.ahmer.accounts.utils.AddCircleIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.EditIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.InfoIcon
import com.ahmer.accounts.utils.MoreIcon
import com.ahmer.accounts.utils.PdfIcon
import com.ahmer.accounts.utils.PdfUtils
import com.ahmer.accounts.utils.SearchIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit,
    personId: Int,
    onPopBackStack: () -> Unit,
    personViewModel: PersonViewModel,
    transViewModel: TransViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val mContext: Context = LocalContext.current
    val mCurrency: Currency by settingsViewModel.currentCurrency.collectAsStateWithLifecycle()
    val mPersons = personViewModel.persons.find { it.personsEntity.id == personId }
    val mPerson: PersonsEntity = mPersons?.personsEntity ?: PersonsEntity()
    val mShowDropdownMenu: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val mShowInfoDialog: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val mShowSearch: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mState: TransState by transViewModel.uiState.collectAsStateWithLifecycle()
    var mShowDeleteDialog: Boolean by remember { mutableStateOf(value = false) }
    var mTextSearch: String by remember { mutableStateOf(value = transViewModel.searchQuery.value) }

    val mLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val mUri = it.data?.data ?: return@rememberLauncherForActivityResult
            transViewModel.generatePdf(
                context = mContext, uri = mUri, person = mPerson, transSum = mState.transSumModel
            )
        }
    }

    LaunchedEffect(key1 = true) {
        transViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (mResult == SnackbarResult.ActionPerformed) {
                        transViewModel.onEvent(TransEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                UiEvent.PopBackStack -> onPopBackStack()

                else -> Unit
            }
        }
    }

    if (mShowInfoDialog.value) {
        MoreInfoAlertDialog(personsEntity = mPerson)
    }

    if (mShowDeleteDialog) {
        DeleteAlertDialog(nameAccount = mPerson.name, onConfirmClick = {
            personViewModel.deletePerson(mPerson)
            onPopBackStack()
        })
    }

    Scaffold(
        modifier = Modifier,
        snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
    ) { innerPadding ->
        val mIconSize: Dp = 36.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 64.dp)
                    .border(BorderStroke(width = 2.dp, color = Color.LightGray.copy(alpha = 0.2f))),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (mShowSearch.value) {
                    SearchBarTransactions(
                        text = mTextSearch, onTextChange = {
                            transViewModel.onEvent(TransEvent.OnSearchTextChange(it))
                            mTextSearch = it
                        }, isShowSearch = mShowSearch
                    )
                }

                if (!mShowSearch.value) {
                    IconButton(
                        onClick = { onPopBackStack() }, modifier = Modifier.size(size = mIconSize)
                    ) { BackIcon() }
                    Text(
                        text = mPerson.name,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    EditIcon(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(size = 18.dp)
                            .clickable { })
                    DeleteIcon(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(size = 18.dp)
                            .clickable { mShowDeleteDialog = true }, tint = Color.Red
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { mShowSearch.value = !mShowSearch.value },
                            modifier = Modifier.size(size = mIconSize)
                        ) { SearchIcon() }
                        IconButton(
                            onClick = { transViewModel.onEvent(TransEvent.OnAddClick) },
                            modifier = Modifier.size(size = mIconSize)
                        ) { AddCircleIcon() }
                        Box {
                            IconButton(
                                onClick = { mShowDropdownMenu.value = !mShowDropdownMenu.value },
                                modifier = Modifier.size(size = mIconSize)
                            ) { MoreIcon() }
                            if (mShowDropdownMenu.value) {
                                ShowDropDown(
                                    context = mContext,
                                    state = mState,
                                    launcher = mLauncher,
                                    showInfoDialog = mShowInfoDialog,
                                    expandMenu = mShowDropdownMenu,
                                )
                            }
                        }
                    }
                }
            }

            PersonTotalBalance(transSumModel = mState.transSumModel, currency = mCurrency)

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                shape = RoundedCornerShape(size = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 2.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.label_description).uppercase(),
                        modifier = Modifier.weight(weight = 0.5f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(id = R.string.label_credit).uppercase(),
                        modifier = Modifier.weight(weight = 0.25f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(id = R.string.label_debit).uppercase(),
                        modifier = Modifier.weight(weight = 0.25f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(space = 3.dp)
            ) {
                items(
                    items = mState.allTransactions,
                    key = { listTrans -> listTrans.id }) { transaction ->
                    TransItem(
                        currency = mCurrency,
                        transEntity = transaction,
                        onEvent = transViewModel::onEvent,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = Constants.ANIMATE_ITEM_DURATION
                            )
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBarTransactions(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    isShowSearch: MutableState<Boolean>,
) = Box(modifier = modifier) {
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mInteractionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .padding(start = 8.dp, end = 8.dp)
                .border(
                    border = BorderStroke(width = 2.dp, color = Color.LightGray),
                    shape = RoundedCornerShape(percent = 50)
                )
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(56.dp)
                    .focusRequester(focusRequester = mFocusRequester)
                    .onFocusChanged { focus ->
                        if (focus.isFocused) {
                            mKeyboardController?.show()
                        }
                    },
                placeholder = { Text(stringResource(id = R.string.label_search)) },
                leadingIcon = { SearchIcon() },
                trailingIcon = {
                    CloseIcon(modifier = Modifier.clickable {
                        mCoroutineScope.launch { delay(duration = 200.milliseconds) }
                        if (text.isNotEmpty()) onTextChange("") else {
                            mFocusManager.clearFocus()
                            isShowSearch.value = false
                        }
                    })
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onTextChange(text)
                    mKeyboardController?.hide()
                    mFocusManager.clearFocus()
                    isShowSearch.value = false
                }),
                singleLine = true,
                maxLines = 1,
                interactionSource = mInteractionSource,
                shape = RoundedCornerShape(size = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}

@Composable
fun ShowDropDown(
    context: Context,
    state: TransState,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    showInfoDialog: MutableState<Boolean>,
    expandMenu: MutableState<Boolean>,
) {
    DropdownMenu(expanded = expandMenu.value, onDismissRequest = { expandMenu.value = false }) {
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.label_generate_pdf)) },
            onClick = {
                val mIntent = PdfUtils.exportToPdf(
                    context = context, transList = state.allTransactions
                )
                if (mIntent != null) {
                    launcher.launch(mIntent)
                }
                expandMenu.value = false
            },
            leadingIcon = { PdfIcon() })
        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.label_info)) },
            onClick = {
                showInfoDialog.value = true
                expandMenu.value = false
            },
            leadingIcon = { InfoIcon() })
    }
}