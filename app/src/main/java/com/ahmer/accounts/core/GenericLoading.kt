package com.ahmer.accounts.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R

@Composable
fun GenericLoading(
    message: String? = null,
    isDialog: Boolean = false
) {
    Column(
        modifier = if (isDialog) Modifier
            .fillMaxWidth()
            .padding(16.dp) else Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.then(Modifier.size(80.dp)),
            strokeWidth = 8.dp
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = message ?: stringResource(id = R.string.title_generic_loading))
    }
}