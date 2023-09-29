package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.core.AsyncData
import com.ahmer.accounts.event.PersonAddEditEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.state.AppBarState
import com.ahmer.accounts.ui.components.PersonAddEditTextFields
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.SaveIcon
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PersonAddEditScreen(
    onPopBackStack: () -> Unit, appBarState: (AppBarState) -> Unit, modifier: Modifier = Modifier
) {
    val mContext: Context = LocalContext.current
    val mViewModel: PersonAddEditViewModel = hiltViewModel()
    val mState by mViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        mViewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.SaveSuccess -> onPopBackStack()
                is UiEvent.ShowToast -> HelperUtils.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = true) {
        appBarState(
            AppBarState(
                searchActions = {
                    Text(
                        text = mViewModel.titleBar,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {},
                floatingAction = {
                    FloatingActionButton(onClick = {
                        mViewModel.onEvent(PersonAddEditEvent.OnSaveClick)
                    }) { SaveIcon() }
                },
                isMenuNavigationIcon = false,
                newNavigationIcon = { IconButton(onClick = { onPopBackStack() }) { BackIcon() } },
                isSnackBarRequired = false,
                newSnackBarHost = {}
            )
        )
    }

    Box {
        AsyncData(resultState = mState.getPersonDetails) {
            mViewModel.currentPerson?.let { personEntity ->
                PersonAddEditTextFields(
                    modifier = modifier, personsEntity = personEntity, onEvent = mViewModel::onEvent
                )
            }
        }
    }
}