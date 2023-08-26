package com.rose.account.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rose.account.R

@Composable
fun CustomSearchView(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSearchClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(20.dp)
            .clip(CircleShape)
            .background(Color(0XFF101921))

    ) {
        TextField(value = text,
            onValueChange = onTextChange,
            colors = TextFieldDefaults.colors(
                //containerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClick(text) }),
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.content_description_search)
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                onTextChange("")
                            } else {
                                onCloseClick()
                            }
                        },
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.content_description_close)
                    )
                }
            }
        )
    }
}