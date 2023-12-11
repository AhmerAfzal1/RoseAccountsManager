package com.ahmer.accounts.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.state.ExpenseState

@Composable
fun ExpensesScreen(viewModel: ExpenseViewModel) {
    val mContext: Context = LocalContext.current
    val mState: State<ExpenseState> = viewModel.uiState.collectAsStateWithLifecycle()

    Toast.makeText(mContext, "This feature is under progress", Toast.LENGTH_LONG).show()
}