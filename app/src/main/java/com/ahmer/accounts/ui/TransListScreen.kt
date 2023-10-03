package com.ahmer.accounts.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.drawer.TopAppBarSearchBox
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.AppBarState
import com.ahmer.accounts.ui.components.TransList
import com.ahmer.accounts.utils.AddCircleIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MoreIcon
import com.ahmer.accounts.utils.PdfIcon
import com.ahmer.accounts.utils.PdfUtils
import com.ahmer.accounts.utils.SearchIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TransListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
    appBarState: (AppBarState) -> Unit
) {
    val mContext: Context = LocalContext.current
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: TransViewModel = hiltViewModel()
    val mState by mViewModel.uiState.collectAsState()
    var mShowDropdownMenu by remember { mutableStateOf(value = false) }
    var mShowSearch by remember { mutableStateOf(value = false) }
    var mTextSearch by remember { mutableStateOf(value = mViewModel.searchQuery.value) }

    val mLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val mUri = it.data?.data ?: return@rememberLauncherForActivityResult
            mViewModel.generatePdf(mContext, mUri)
        }
    }

    LaunchedEffect(key1 = true) {
        mViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (mResult == SnackbarResult.ActionPerformed) {
                        mViewModel.onEvent(TransEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = true) {
        appBarState(
            AppBarState(
                searchActions = {
                    if (mShowSearch) {
                        TopAppBarSearchBox(text = mTextSearch, onTextChange = {
                            mViewModel.onEvent(TransEvent.OnSearchTextChange(it))
                            mTextSearch = it
                        }, onCloseClick = {
                            mCoroutineScope.launch { delay(duration = 200.milliseconds) }
                            mShowSearch = false
                        })
                    } else {
                        Text(
                            text = "All Transactions",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    if (!mShowSearch) {
                        IconButton(onClick = { mShowSearch = true }) { SearchIcon() }
                    }
                    IconButton(onClick = { mViewModel.onEvent(TransEvent.OnAddClick) }) { AddCircleIcon() }
                    if (!mShowSearch) {
                        IconButton(onClick = {
                            mShowDropdownMenu = !mShowDropdownMenu
                        }) { MoreIcon() }
                    }
                    DropdownMenu(
                        expanded = mShowDropdownMenu,
                        onDismissRequest = { mShowDropdownMenu = false }) {
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.label_generate_pdf)) },
                            onClick = {
                                val mIntent = PdfUtils.exportToPdf(
                                    context = mContext, transList = mState.getAllPersonsTransList
                                )
                                if (mIntent != null) {
                                    mLauncher.launch(mIntent)
                                }
                                mShowDropdownMenu = false
                            },
                            leadingIcon = { PdfIcon() })
                    }
                },
                floatingAction = {},
                isMenuNavigationIcon = false,
                newNavigationIcon = { IconButton(onClick = { onPopBackStack() }) { BackIcon() } },
                isSnackBarRequired = true,
                newSnackBarHost = { SnackbarHost(hostState = mSnackBarHostState) }
            )
        )
    }

    TransList(
        transListState = mState.getAllPersonsTransList,
        transSumModel = mState.getPersonTransBalance,
        onEvent = mViewModel::onEvent,
        reloadData = mViewModel::getAllPersonsTransactions
    )
}