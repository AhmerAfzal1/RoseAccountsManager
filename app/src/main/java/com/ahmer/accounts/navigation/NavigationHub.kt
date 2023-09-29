package com.ahmer.accounts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmer.accounts.state.AppBarState
import com.ahmer.accounts.ui.PersonAddEditScreen
import com.ahmer.accounts.ui.PersonsListScreen
import com.ahmer.accounts.ui.SettingsScreen
import com.ahmer.accounts.ui.TransAddEditScreen
import com.ahmer.accounts.ui.TransListScreen

@Composable
fun MainNavigation(
    navHostController: NavHostController,
    appBarState: (AppBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navHostController,
        startDestination = ScreenRoutes.PersonListScreen,
        modifier = modifier,
        builder = {
            composable(route = ScreenRoutes.PersonListScreen) {
                PersonsListScreen(appBarState = appBarState, onNavigation = {
                    navHostController.navigate(it.route) /*{
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
                PersonAddEditScreen(
                    onPopBackStack = { navHostController.popBackStack() },
                    appBarState = appBarState,
                )
            }
            composable(route = ScreenRoutes.SettingsScreen) {
                SettingsScreen(appBarState = appBarState)
            }
            composable(
                route = ScreenRoutes.TransListScreen + "?transPersonId={transPersonId}",
                arguments = listOf(navArgument(name = "transPersonId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                TransListScreen(
                    onNavigation = { navHostController.navigate(it.route) },
                    onPopBackStack = { navHostController.popBackStack() },
                    appBarState = appBarState,
                )
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
                TransAddEditScreen(
                    onPopBackStack = { navHostController.popBackStack() },
                    appBarState = appBarState,
                )
            }
        })
}