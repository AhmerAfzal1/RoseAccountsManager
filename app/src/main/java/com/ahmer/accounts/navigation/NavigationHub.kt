package com.ahmer.accounts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmer.accounts.database.state.Routes
import com.ahmer.accounts.ui.AddOrEditScreen
import com.ahmer.accounts.ui.TopAppBarWithNavigationBar

@Composable
fun MainNavigation() {
    val mNavHostController = rememberNavController()

    NavHost(navController = mNavHostController, startDestination = Routes.HOME_SCREEN, builder = {
        composable(route = Routes.HOME_SCREEN) {
            TopAppBarWithNavigationBar(onNavigation = {
                mNavHostController.navigate(it.route)
            })
        }
        composable(
            route = Routes.ADD_EDIT_SCREEN + "?userId={userId}",
            arguments = listOf(navArgument(name = "userId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            AddOrEditScreen(onPopBackStack = {
                mNavHostController.popBackStack()
            })
        }
    })
}