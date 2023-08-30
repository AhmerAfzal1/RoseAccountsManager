package com.ahmer.accounts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmer.accounts.ui.AddOrEditScreen
import com.ahmer.accounts.ui.TopAppBarWithNavigationBar

@Composable
fun MainNavigation() {
    val mNavHostController = rememberNavController()

    NavHost(
        navController = mNavHostController,
        startDestination = NavScreens.HomeScreen.route,
        builder = {
            composable(NavScreens.HomeScreen.route) {
                TopAppBarWithNavigationBar(navHostController = mNavHostController)
            }
            composable(NavScreens.AddEditScreen.route) {
                AddOrEditScreen(navHostController = mNavHostController)
            }
        }
    )
}