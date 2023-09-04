package com.ahmer.accounts.core

import androidx.compose.runtime.Composable

@Composable
fun <T> AsyncData(
    resultState: ResultState<T?>?,
    loadingContent: @Composable () -> Unit = { GenericLoading() },
    errorContent: @Composable () -> Unit = { GenericError() },
    content: @Composable (data: T?) -> Unit
) {
    resultState.let { state ->
        when (state) {
            is ResultState.Loading -> {
                loadingContent()
            }

            is ResultState.Error -> {
                errorContent()
            }

            null -> {
                content(null)
            }

            is ResultState.Success -> {
                content(state.data)
            }
        }
    }
}