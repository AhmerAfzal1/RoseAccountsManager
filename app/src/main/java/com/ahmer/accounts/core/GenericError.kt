package com.ahmer.accounts.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmer.accounts.R
import com.ahmer.accounts.utils.ErrorIcon

@Composable
fun GenericError(
    error: ErrorEntity? = null,
    dismissText: String? = null,
    onDismissAction: (() -> Unit)? = null,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorIcon(
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(text = error?.message ?: stringResource(id = R.string.label_generic_error))
        onDismissAction?.let {
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = onDismissAction) {
                Text(text = dismissText ?: stringResource(id = R.string.label_ok))
            }
        }
    }
}