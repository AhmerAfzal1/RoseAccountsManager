package com.ahmer.accounts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.ahmer.accounts.database.model.PersonsEntity
import com.ahmer.accounts.event.PersonEvent

@Composable
fun PersonsList(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    personsListState: ResultState<List<PersonsEntity>>,
    onEvent: (PersonEvent) -> Unit,
    reloadData: () -> Unit
) {
    Box(Modifier.padding(padding)) {
        AsyncData(resultState = personsListState, errorContent = {
            GenericError(
                onDismissAction = reloadData
            )
        }) { personsList ->
            personsList?.let {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(
                        items = personsList,
                        key = { persons -> persons.id }) { person ->
                        PersonItem(personsEntity = person, onEvent = onEvent)
                    }
                }
            }
        }
    }
}