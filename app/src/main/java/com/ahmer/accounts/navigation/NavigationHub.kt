package com.ahmer.accounts.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmer.accounts.ui.PersonAddEditScreen
import com.ahmer.accounts.ui.PersonsListScreen
import com.ahmer.accounts.ui.SettingsScreen
import com.ahmer.accounts.ui.TransAddEditScreen
import com.ahmer.accounts.ui.TransListScreen

@Composable
fun MainNavigation(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavItems.Accounts.fullRoute,
        modifier = modifier
    ) {
        composable(
            route = NavItems.Accounts.fullRoute,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            PersonsListScreen(onNavigation = { navController.navigate(it.route) })
        }
        composable(
            route = NavItems.Settings.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            SettingsScreen()
        }
        composable(
            route = NavItems.PersonAddEdit.fullRoute,
            arguments = listOf(navArgument(name = "personId") {
                type = NavType.IntType
                defaultValue = -1
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            PersonAddEditScreen(onPopBackStack = { navController.popBackStack() })
        }
        composable(
            route = NavItems.Transactions.fullRoute,
            arguments = listOf(navArgument(name = "transPersonId") {
                type = NavType.IntType
                defaultValue = -1
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            TransListScreen(
                onNavigation = { navController.navigate(it.route) },
                onPopBackStack = { navController.popBackStack() },
            )
        }
        composable(
            route = NavItems.TransactionsAddEdit.fullRoute,
            arguments = listOf(navArgument(name = "transId") {
                type = NavType.IntType
                defaultValue = -1
            }, navArgument(name = "transPersonId") {
                type = NavType.IntType
                defaultValue = -1
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            TransAddEditScreen(onPopBackStack = { navController.popBackStack() })
        }
    }
}