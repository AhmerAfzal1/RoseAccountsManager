package com.ahmer.accounts.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.drawer.TopAppBarSearchBox
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.ui.components.TransItem
import com.ahmer.accounts.ui.components.TransTotal
import com.ahmer.accounts.utils.AddCircleIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.Constants
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
) {
    val mContext: Context = LocalContext.current
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: TransViewModel = hiltViewModel()
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
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

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
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
                navigationIcon = { IconButton(onClick = { onPopBackStack() }) { BackIcon() } },
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                scrollBehavior = mScrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
    ) { innerPadding ->
        Box(contentAlignment = Alignment.BottomCenter) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(space = 3.dp)
            ) {
                items(
                    items = mState.getAllPersonsTransList,
                    key = { listTrans -> listTrans.id }) { transaction ->
                    TransItem(
                        transEntity = transaction,
                        onEvent = mViewModel::onEvent,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = Constants.ANIMATE_ITEM_DURATION
                            )
                        )
                    )
                }
            }

            LazyColumn {
                item { TransTotal(transSumModel = mState.getPersonTransBalance) }
            }
        }
    }
}