package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.Constants

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddEditScreen(viewModel: ExpenseAddEditViewModel, onPopBackStack: () -> Unit) {
    val mContext: Context = LocalContext.current
    val mFocusManager: FocusManager = LocalFocusManager.current
    val mFocusRequester: FocusRequester = remember { FocusRequester() }
    val mKeyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    val mSurfaceColor: Color =
        if (MaterialTheme.colorScheme.isLight()) Color.Black else Color.Yellow
    val mSurfaceElevation: Dp = 4.dp

    LaunchedEffect(Unit) {
        mFocusRequester.requestFocus()
    }

    fun clear() {
        mFocusManager.clearFocus()
        mKeyboardController?.hide()
    }

    Scaffold(modifier = Modifier, topBar = {
        Surface(
            modifier = Modifier.shadow(
                elevation = mSurfaceElevation,
                ambientColor = mSurfaceColor,
                spotColor = mSurfaceColor,
            )
        ) {
            TopAppBar(
                title = { Text(text = viewModel.titleBar) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            clear()
                            onPopBackStack()
                        },
                        modifier = Modifier.size(size = Constants.ICON_SIZE)
                    ) { BackIcon() }
                },
            )
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top
        ) {
            ExpenseAddEditMain(
                viewModel = viewModel,
                focusManager = mFocusManager,
                focusRequester = mFocusRequester,
                keyboardController = mKeyboardController
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExpenseAddEditMain(
    viewModel: ExpenseAddEditViewModel,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
) {

    fun clear() {
        focusManager.clearFocus()
        keyboardController?.hide()
    }
}