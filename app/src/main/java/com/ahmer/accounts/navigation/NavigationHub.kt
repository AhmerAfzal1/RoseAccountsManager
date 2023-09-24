package com.ahmer.accounts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ahmer.accounts.ui.PersonAddEditScreen
import com.ahmer.accounts.ui.PersonsListScreen
import com.ahmer.accounts.ui.SettingsScreen
import com.ahmer.accounts.ui.TransAddEditScreen
import com.ahmer.accounts.ui.TransListScreen

@Composable
fun MainNavigation() {
    val mNavHostController = rememberNavController()

    NavHost(navController = mNavHostController,
        startDestination = ScreenRoutes.PersonListScreen,
        builder = {
            composable(route = ScreenRoutes.PersonListScreen) {
                PersonsListScreen(onNavigation = {
                    mNavHostController.navigate(it.route) /*{
                        when (it.route) {
                            ScreenRoutes.PersonListScreen -> {
                                popUpTo(ScreenRoutes.PersonListScreen)
                            }

                            ScreenRoutes.SettingsScreen -> {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }*/
                })
            }
            composable(
                route = ScreenRoutes.PersonAddEditScreen + "?personId={personId}",
                arguments = listOf(navArgument(name = "personId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                PersonAddEditScreen(onPopBackStack = { mNavHostController.popBackStack() })
            }
            composable(route = ScreenRoutes.SettingsScreen) {
                SettingsScreen(onPopBackStack = { mNavHostController.popBackStack() })
            }
            composable(
                route = ScreenRoutes.TransListScreen + "?transPersonId={transPersonId}",
                arguments = listOf(navArgument(name = "transPersonId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                TransListScreen(onNavigation = { mNavHostController.navigate(it.route) },
                    onPopBackStack = { mNavHostController.popBackStack() })
            }
            composable(
                route = ScreenRoutes.TransAddEditScreen + "?transId={transId}/transPersonId={transPersonId}",
                arguments = listOf(navArgument(name = "transId") {
                    type = NavType.IntType
                    defaultValue = -1
                }, navArgument(name = "transPersonId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                TransAddEditScreen(onPopBackStack = { mNavHostController.popBackStack() })
            }
        })
}