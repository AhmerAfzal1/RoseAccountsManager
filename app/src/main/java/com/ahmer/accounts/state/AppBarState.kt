package com.ahmer.accounts.state

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

data class AppBarState(
    val searchActions: @Composable () -> Unit = {},
    val actions: @Composable RowScope.() -> Unit = {},
    val floatingAction: @Composable () -> Unit = {},
    val isMenuNavigationIcon: Boolean = false,
    val newNavigationIcon: @Composable () -> Unit = {},
    val isSnackBarRequired: Boolean = false,
    val newSnackBarHost: @Composable () -> Unit = {},
)