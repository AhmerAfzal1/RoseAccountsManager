package com.ahmer.accounts.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.ahmer.accounts.R
import com.ahmer.accounts.utils.CloseIcon
import com.ahmer.accounts.utils.SearchIcon

@Composable
fun TopAppBarSearchBox(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text(stringResource(id = R.string.label_search)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onTextChange(text) }),
            leadingIcon = { SearchIcon() },
            trailingIcon = {
                CloseIcon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) onTextChange("") else onCloseClick()
                    }
                )
            }
        )
    }
}