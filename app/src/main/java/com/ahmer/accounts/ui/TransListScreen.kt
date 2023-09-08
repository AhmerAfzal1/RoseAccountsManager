package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.dialogs.TransAddEditAlertDialog
import com.ahmer.accounts.event.TransAddEditEvent
import com.ahmer.accounts.event.TransEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.ui.components.TransList
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.HelperFunctions
import com.ahmer.accounts.utils.SearchIcon
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit
) {
    val mContext: Context = LocalContext.current
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: TransViewModel = hiltViewModel()
    val mState by mViewModel.uiState.collectAsState()

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

                is UiEvent.ShowToast -> HelperFunctions.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = "All Transactions", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }, navigationIcon = {
            IconButton(onClick = { onPopBackStack() }) { BackIcon() }
        }, actions = {
            IconButton(onClick = { mViewModel.onEvent(TransEvent.OnAddClick) }) { AddIcon() }
            IconButton(onClick = { /*TODO*/ }) { SearchIcon() }
        },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ), scrollBehavior = mScrollBehavior
        )
    }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) }) { innerPadding ->
        TransList(
            padding = innerPadding,
            transListState = mState.getAllUsersTransList,
            onEvent = mViewModel::onEvent,
            reloadData = mViewModel::getAllUserTransactions
        )
    }
}