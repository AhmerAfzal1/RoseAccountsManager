package com.rose.account.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.rose.account.R
import com.rose.account.database.AppDatabase
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    //onSearchClick: (String) -> Unit,
    onDismiss: () -> Unit
) {

    var mIsActive by rememberSaveable { mutableStateOf(false) }
    val mFocusManager = LocalFocusManager.current

    fun closeSearchBar() {
        mFocusManager.clearFocus()
        mIsActive = false
        onDismiss()
    }

    LaunchedEffect(Unit) {
        delay(200)
        mIsActive = true
    }

    SearchBar(
        modifier = modifier.fillMaxWidth(),
        query = text,
        onQueryChange = onTextChange,
        onSearch = { closeSearchBar() },
        active = mIsActive,
        onActiveChange = {
            mIsActive = it
            if (!mIsActive) {
                mFocusManager.clearFocus()
                onDismiss()
            }
        },
        placeholder = { Text("Search") },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = stringResource(id = R.string.content_description_search)
            )
        },
        trailingIcon = {
            if (mIsActive) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            closeSearchBar()
                        }
                    },
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.content_description_close)
                )
            }
        },
    ) {
        /*repeat(4) { idx ->
            val resultText = "Suggestion $idx"
            ListItem(
                headlineContent = { Text(resultText) },
                supportingContent = { Text("Additional info") },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                modifier = Modifier
                    .clickable {
                        query = resultText
                        mIsActive = false
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }*/
    }
}