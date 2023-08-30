package com.ahmer.accounts.navigation

sealed class NavScreens(val route: String) {
    object HomeScreen : NavScreens("HomeRoute")
    object AddEditScreen : NavScreens("AddEditRoute")
}
