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
import com.ahmer.accounts.ui.ExpenseAddEditScreen
import com.ahmer.accounts.ui.ExpenseListScreen
import com.ahmer.accounts.ui.PersonAddEditScreen
import com.ahmer.accounts.ui.PersonsListScreen
import com.ahmer.accounts.ui.ReportScreen
import com.ahmer.accounts.ui.SettingsScreen
import com.ahmer.accounts.ui.TransAddEditScreen
import com.ahmer.accounts.ui.TransListScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier, navController: NavHostController, transSumModel: TransSumModel
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
                onNavigation = { navController.navigate(route = it.route) },
                personViewModel = hiltViewModel(),
                settingsViewModel = hiltViewModel(),
                transSumModel = transSumModel,
            )
        }
        composable(
            route = NavItems.Expense.fullRoute,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            ExpenseListScreen(
                onNavigation = { navController.navigate(route = it.route) },
                viewModel = hiltViewModel(),
            )
        }
        composable(
            route = NavItems.ExpenseAddEdit.fullRoute,
            arguments = listOf(navArgument(name = "expenseID") {
                type = NavType.IntType
                defaultValue = -1
            }),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            ExpenseAddEditScreen(
                viewModel = hiltViewModel(),
                viewModelSettings = hiltViewModel(),
                onPopBackStack = { navController.popBackStack() }
            )
        }
        composable(
            route = NavItems.Report.fullRoute,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            ReportScreen(
                mainViewModel = hiltViewModel(),
                viewModel = hiltViewModel()
            )
        }
        composable(
            route = NavItems.Settings.fullRoute,
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
        ) { backStackEntry ->
            val personId: Int? = backStackEntry.arguments?.getInt("transPersonId")
            personId?.let { id ->
                TransListScreen(
                    onNavigation = { navController.navigate(route = it.route) },
                    personId = id,
                    onPopBackStack = { navController.popBackStack() },
                    personViewModel = hiltViewModel(),
                    transViewModel = hiltViewModel(),
                    settingsViewModel = hiltViewModel()
                )
            }
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
                viewModelSettings = hiltViewModel(),
                onPopBackStack = { navController.popBackStack() }
            )
        }
    }
}