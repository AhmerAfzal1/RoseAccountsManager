package com.ahmer.accounts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmer.accounts.ui.TransListScreen
import com.ahmer.accounts.ui.UserAddEditScreen
import com.ahmer.accounts.ui.UsersListScreen

@Composable
fun MainNavigation() {
    val mNavHostController = rememberNavController()

    NavHost(
        navController = mNavHostController,
        startDestination = ScreenRoutes.UserListScreen,
        builder = {
            composable(route = ScreenRoutes.UserListScreen) {
                UsersListScreen(onNavigation = {
                    mNavHostController.navigate(it.route)
                })
            }
            composable(
                route = ScreenRoutes.UserAddEditScreen + "?userId={userId}",
                arguments = listOf(navArgument(name = "userId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                UserAddEditScreen(onPopBackStack = {
                    mNavHostController.popBackStack()
                })
            }
            composable(
                route = ScreenRoutes.TransListScreen + "?transUserId={transUserId}",
                arguments = listOf(navArgument(name = "transUserId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                TransListScreen(
                    onNavigation = { mNavHostController.navigate(it.route) },
                    onPopBackStack = { mNavHostController.popBackStack() }
                )
            }
        }
    )
}