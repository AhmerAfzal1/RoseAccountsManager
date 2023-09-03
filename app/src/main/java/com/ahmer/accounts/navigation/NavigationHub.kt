package com.ahmer.accounts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmer.accounts.ui.AddOrEditScreen
import com.ahmer.accounts.ui.HomeScreen

@Composable
fun MainNavigation() {
    val mNavHostController = rememberNavController()

    NavHost(
        navController = mNavHostController,
        startDestination = ScreenRoutes.HomeScreen,
        builder = {
            composable(route = ScreenRoutes.HomeScreen) {
                HomeScreen(onNavigation = {
                    mNavHostController.navigate(it.route)
                })
            }
            composable(
                route = ScreenRoutes.AddEditScreen + "?userId={userId}",
                arguments = listOf(navArgument(name = "userId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                AddOrEditScreen(onPopBackStack = {
                    mNavHostController.popBackStack()
                })
            }
        }
    )
}