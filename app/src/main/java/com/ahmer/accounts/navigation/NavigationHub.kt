package com.ahmer.accounts.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmer.accounts.database.model.TransSumModel
import com.ahmer.accounts.ui.PersonAddEditScreen
import com.ahmer.accounts.ui.PersonsListScreen
import com.ahmer.accounts.ui.SettingsScreen
import com.ahmer.accounts.ui.TransAddEditScreen
import com.ahmer.accounts.ui.TransListScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    transSumModel: TransSumModel
) {
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
            PersonsListScreen(
                onNavigation = { navController.navigate(it.route) },
                viewModel = hiltViewModel(),
                transSumModel = transSumModel,
            )
        }
        composable(
            route = NavItems.Settings.route,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            SettingsScreen(viewModel = hiltViewModel())
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
            PersonAddEditScreen(
                viewModel = hiltViewModel(),
                onPopBackStack = { navController.popBackStack() }
            )
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
                viewModel = hiltViewModel(),
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
            TransAddEditScreen(
                viewModel = hiltViewModel(),
                onPopBackStack = { navController.popBackStack() }
            )
        }
    }
}