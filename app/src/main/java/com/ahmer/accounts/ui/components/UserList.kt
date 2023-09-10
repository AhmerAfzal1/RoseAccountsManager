package com.ahmer.accounts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.core.AsyncData
import com.ahmer.accounts.core.GenericError
import com.ahmer.accounts.core.ResultState
import com.ahmer.accounts.database.model.UserModel
import com.ahmer.accounts.event.UserEvent

@Composable
fun UsersList(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    usersListState: ResultState<List<UserModel>>,
    onEvent: (UserEvent) -> Unit,
    reloadData: () -> Unit
) {
    Box(Modifier.padding(padding)) {
        AsyncData(resultState = usersListState, errorContent = {
            GenericError(
                onDismissAction = reloadData
            )
        }) { usersList ->
            usersList?.let {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = usersList,
                            key = { listUser -> listUser.id }) { user ->
                            UserItem(userModel = user, onEvent = onEvent)
                        }
                    }
                }
            }
        }
    }
}