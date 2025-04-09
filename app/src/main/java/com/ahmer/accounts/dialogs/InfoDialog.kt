package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.database.entity.PersonEntity
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.InfoIcon

@Composable
private fun RowScope.InfoText(text: String, weight: Float, isTitle: Boolean = false) {
    Text(
        modifier = Modifier
            .weight(weight)
            .padding(
                start = if (!isTitle) 2.dp else 0.dp,
                top = 3.dp
            ),
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = if (isTitle) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun MoreInfoAlertDialog(personEntity: PersonEntity) {
    val mDataList: List<Pair<String, String>> by lazy {
        listOf(
            "Name:" to personEntity.name,
            "Phone:" to personEntity.phone,
            "Address:" to personEntity.address,
            "Email:" to personEntity.email,
            "Notes:" to personEntity.notes,
            "Created:" to HelperUtils.getDateTime(time = personEntity.created),
            "Updated:" to HelperUtils.getDateTime(time = personEntity.updated),
        )
    }
    var mOpenDialog: Boolean by remember { mutableStateOf(value = true) }

    if (mOpenDialog) {
        AlertDialog(
            onDismissRequest = { mOpenDialog = false },
            confirmButton = {
                TextButton(onClick = { mOpenDialog = false }) {
                    Text(text = stringResource(id = R.string.label_ok))
                }
            },
            icon = { InfoIcon() },
            text = {
                LazyColumn {
                    items(items = mDataList) { (title, value) ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            InfoText(text = title, weight = 1f, isTitle = true)
                            InfoText(text = value, weight = 3f, isTitle = false)
                        }
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
        )
    }
}