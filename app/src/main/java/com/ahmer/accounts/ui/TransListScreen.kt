package com.ahmer.accounts.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity
import com.ahmer.accounts.dialogs.DeleteAlertDialog
import com.ahmer.accounts.dialogs.MoreInfoAlertDialog
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.TransState
import com.ahmer.accounts.ui.components.ItemBalance
import com.ahmer.accounts.ui.components.ItemTrans
import com.ahmer.accounts.utils.AddCircleIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.DeleteIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.PdfUtils
import com.ahmer.accounts.utils.SearchIcon
import com.ahmer.accounts.utils.SelectAllIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
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
    val mSelectedItems: SnapshotStateList<TransEntity> = remember { mutableStateListOf() }
    val isNotSelection: Boolean = mSelectedItems.size <= 0
    val mShowSearch: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mState: TransState by transViewModel.uiState.collectAsStateWithLifecycle()
    val mSurfaceColor: Color =
        if (MaterialTheme.colorScheme.isLight()) Color.Black else Color.Yellow
    val mSurfaceElevation: Dp = 4.dp
    var mLongClickState: Boolean by remember { mutableStateOf(value = false) }
    var mShowDeleteDialogAccount: Boolean by remember { mutableStateOf(value = false) }
    var mShowDeleteDialogTrans: Boolean by remember { mutableStateOf(value = false) }
    var mShowInfoDialog: Boolean by remember { mutableStateOf(value = false) }
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

    if (mShowInfoDialog) {
        MoreInfoAlertDialog(personsEntity = mPerson)
    }

    if (mShowDeleteDialogAccount) {
        DeleteAlertDialog(accountName = mPerson.name, transactionsList = emptyList()) {
            personViewModel.deletePerson(person = mPerson)
            onPopBackStack()
        }
    }

    if (mShowDeleteDialogTrans) {
        DeleteAlertDialog(transactionsList = mSelectedItems.toList()) {
            for (i in mSelectedItems.toList()) {
                transViewModel.onEvent(TransEvent.OnDeleteClick(i))
            }
            onPopBackStack()
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            Surface(
                modifier = if (mShowSearch.value) {
                    Modifier
                        .height(height = 64.dp)
                        .shadow(
                            elevation = mSurfaceElevation,
                            ambientColor = mSurfaceColor,
                            spotColor = mSurfaceColor,
                        )
                } else {
                    Modifier.shadow(
                        elevation = mSurfaceElevation,
                        ambientColor = mSurfaceColor,
                        spotColor = mSurfaceColor,
                    )
                },
            ) {
                if (!mShowSearch.value) {
                    TopAppBar(
                        title = {
                            Text(
                                text = if (isNotSelection) "All Transactions"
                                else "Selected: ${mSelectedItems.size}"
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                if (isNotSelection) {
                                    mSelectedItems.clear()
                                    onPopBackStack()
                                } else mSelectedItems.clear()
                            }) { BackIcon() }
                        },
                        actions = {
                            if (isNotSelection) {
                                IconButton(
                                    onClick = { mShowSearch.value = !mShowSearch.value },
                                    modifier = Modifier.size(size = Constants.ICON_SIZE)
                                ) { SearchIcon() }
                                IconButton(
                                    onClick = { transViewModel.onEvent(TransEvent.OnAddClick) },
                                    modifier = Modifier.size(size = Constants.ICON_SIZE)
                                ) { AddCircleIcon() }
                            } else {
                                IconButton(
                                    onClick = { mShowDeleteDialogTrans = !mShowDeleteDialogTrans },
                                    modifier = Modifier.size(size = Constants.ICON_SIZE)
                                ) { DeleteIcon(tint = Color.Red) }

                                IconButton(
                                    onClick = {
                                        mSelectedItems.clear()
                                        val mSelect: List<TransEntity> =
                                            mState.allTransactions.filter {
                                                it.personId == personId
                                            }
                                        mSelectedItems.addAll(mSelect)
                                    },
                                    modifier = Modifier.size(size = Constants.ICON_SIZE)
                                ) { SelectAllIcon() }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors()
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SearchBarTransactions(
                            text = mTextSearch, onTextChange = {
                                transViewModel.onEvent(TransEvent.OnSearchTextChange(it))
                                mTextSearch = it
                            }, isShowSearch = mShowSearch
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            ItemBalance(
                transSumModel = mState.transSumModel,
                currency = mCurrency,
                personsEntity = mPerson,
                isUsedTrans = true,
                onClickDelete = { mShowDeleteDialogAccount = true },
                onClickInfo = { mShowInfoDialog = !mShowInfoDialog },
                onClickPdf = {
                    val mIntent = PdfUtils.exportToPdf(
                        context = mContext, transList = mState.allTransactions
                    )
                    if (mIntent != null) {
                        mLauncher.launch(mIntent)
                    }
                },
                onClickEdit = { transViewModel.onEvent(TransEvent.OnPersonEditClick(mPerson)) },
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = mSurfaceElevation,
                        ambientColor = mSurfaceColor,
                        spotColor = mSurfaceColor,
                    ),
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
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = Constants.TYPE_DEBIT_SUFFIX.uppercase(),
                        modifier = Modifier.weight(weight = 0.25f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = Constants.TYPE_CREDIT_SUFFIX.uppercase(),
                        modifier = Modifier.weight(weight = 0.25f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 4.dp, end = 4.dp),
                verticalArrangement = Arrangement.Top
            ) {
                items(
                    items = mState.allTransactions,
                    key = { listTrans -> listTrans.id }
                ) { transaction ->
                    val isSelected = mSelectedItems.contains(transaction)
                    ItemTrans(
                        transEntity = transaction,
                        currency = mCurrency,
                        isSelected = isSelected,
                        onClick = {
                            if (!mLongClickState) {
                                transViewModel.onEvent(TransEvent.OnEditClick(transaction))
                            } else {
                                if (isSelected) mSelectedItems.remove(transaction)
                                else mSelectedItems.add(transaction)
                                if (mSelectedItems.size == 0) mLongClickState = false
                                mShowSearch.value = false
                            }
                        },
                        onLongClick = {
                            mLongClickState = true
                            if (isSelected) mSelectedItems.remove(transaction)
                            else mSelectedItems.add(transaction)
                            if (mSelectedItems.size == 0) mLongClickState = false
                            mShowSearch.value = false
                        },
                        modifier = Modifier.animateItem(
                            fadeInSpec = tween(durationMillis = Constants.ANIMATE_FADE_IN_DURATION),
                            fadeOutSpec = tween(durationMillis = Constants.ANIMATE_FADE_OUT_DURATION),
                            placementSpec = tween(durationMillis = Constants.ANIMATE_DURATION)
                        )
                    )
                }
            }
        }
    }
}

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
                .height(height = 40.dp)
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
                    .requiredHeight(height = 56.dp)
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