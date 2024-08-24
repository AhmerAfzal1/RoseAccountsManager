package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.event.ExpenseEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.ExpenseState
import com.ahmer.accounts.ui.components.ItemExpense
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.HelperUtils
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpenseListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit,
    viewModel: ExpenseViewModel,
    viewModelSettings: SettingsViewModel,
) {
    val mContext: Context = LocalContext.current
    val mCurrency: Currency by viewModelSettings.currentCurrency.collectAsStateWithLifecycle()
    val mState: ExpenseState by viewModel.uiState.collectAsStateWithLifecycle()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    var isVisibleFab: Boolean by rememberSaveable { mutableStateOf(value = true) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (mResult == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(ExpenseEvent.OnUndoDelete)
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
        snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isVisibleFab,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(onClick = { viewModel.onEvent(ExpenseEvent.OnAdd) }) {
                    AddIcon()
                }
            }
        },
    ) { innerPadding ->
        val mNestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y < -1) isVisibleFab = false // Hide FAB
                    if (available.y > 1) isVisibleFab = true // Show FAB
                    return Offset.Zero
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(connection = mNestedScrollConnection),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                items = mState.allExpenses,
                key = { expense -> expense.id }
            ) { expenseEntity ->
                ItemExpense(
                    expenseEntity = expenseEntity,
                    allExpense = mState.allExpenses,
                    currency = mCurrency,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(durationMillis = Constants.ANIMATE_ITEM_DURATION)
                    )
                )
            }
        }
    }
}