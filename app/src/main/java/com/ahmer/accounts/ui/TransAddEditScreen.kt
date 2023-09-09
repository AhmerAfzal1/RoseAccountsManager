package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.core.AsyncData
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.ui.components.TransAddEditTextFields
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.HelperFunctions
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransAddEditScreen(onPopBackStack: () -> Unit, modifier: Modifier = Modifier) {
    val mContext: Context = LocalContext.current
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: TransAddEditViewModel = hiltViewModel()
    val mState by mViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        mViewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.SaveSuccess -> onPopBackStack()
                is UiEvent.ShowToast -> HelperFunctions.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = mViewModel.titleBar, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }, navigationIcon = {
            IconButton(onClick = { onPopBackStack() }) { BackIcon() }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ), scrollBehavior = mScrollBehavior
        )
    }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AsyncData(resultState = mState.getTransDetails) {
                mViewModel.currentTransaction?.let { transaction ->
                    TransAddEditTextFields(
                        transModel = transaction,
                        onEvent = mViewModel::onEvent,
                        titleButton = mViewModel.titleButton
                    )
                }
            }
        }
    }
}