package com.ahmer.accounts.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.ui.components.PersonItem
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.SearchIcon
import com.ahmer.accounts.utils.SettingsIcon
import com.ahmer.accounts.utils.SortByDateIcon
import com.ahmer.accounts.utils.SortByNameIcon
import com.ahmer.accounts.utils.SortIcon
import com.ahmer.accounts.utils.SortOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonsListScreen(onNavigation: (UiEvent.Navigate) -> Unit) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: PersonViewModel = hiltViewModel()
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mState by mViewModel.uiState.collectAsState()
    var mShowDropdownMenu by remember { mutableStateOf(value = false) }
    var mShowSearch by remember { mutableStateOf(value = false) }
    var mTextSearch by remember { mutableStateOf(value = mViewModel.searchQuery.value) }

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
                        mViewModel.onEvent(PersonEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.RelaunchApp -> HelperUtils.relaunchApp(mContext)
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
                        TopAppBarSearchBox(text = mTextSearch, onTextChange = { text ->
                            mViewModel.onEvent(PersonEvent.OnSearchTextChange(text))
                            mTextSearch = text
                        }, onCloseClick = {
                            mCoroutineScope.launch { delay(duration = 200.milliseconds) }
                            mShowSearch = false
                        })
                    } else {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    if (!mShowSearch) {
                        IconButton(onClick = { mShowSearch = true }) { SearchIcon() }
                    }
                    IconButton(onClick = {
                        mShowDropdownMenu = !mShowDropdownMenu
                    }) { SortIcon() }
                    DropdownMenu(expanded = mShowDropdownMenu,
                        onDismissRequest = { mShowDropdownMenu = false }) {
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.label_sort_by_name)) },
                            onClick = {
                                mViewModel.onEvent(PersonEvent.OnSortBy(SortOrder.Name))
                                mShowDropdownMenu = false
                            },
                            leadingIcon = { SortByNameIcon() })
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.label_sort_by_date_created)) },
                            onClick = {
                                mViewModel.onEvent(PersonEvent.OnSortBy(SortOrder.Date))
                                mShowDropdownMenu = false
                            },
                            leadingIcon = { SortByDateIcon() })
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.label_settings)) },
                            onClick = {
                                mViewModel.onEvent(PersonEvent.OnSettingsClick)
                                mShowDropdownMenu = false
                            },
                            leadingIcon = { SettingsIcon() })
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
        floatingActionButton = {
            FloatingActionButton(onClick = { mViewModel.onEvent(PersonEvent.OnNewAddClick) }) {
                AddIcon()
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            contentPadding = PaddingValues(all = 10.dp),
            verticalArrangement = Arrangement.spacedBy(space = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = mState.getAllPersonsList,
                key = { persons -> persons.id }) { person ->
                PersonItem(
                    personsEntity = person,
                    onEvent = mViewModel::onEvent,
                    modifier = Modifier.animateItemPlacement(tween(durationMillis = Constants.ANIMATE_ITEM_DURATION))
                )
            }
        }
    }
}