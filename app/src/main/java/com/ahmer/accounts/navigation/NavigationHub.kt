package com.ahmer.accounts.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmer.accounts.database.model.TransactionSumModel
import com.ahmer.accounts.ui.PersonAddEditScreen
import com.ahmer.accounts.ui.PersonsListScreen
import com.ahmer.accounts.ui.ReportScreen
import com.ahmer.accounts.ui.SettingsScreen
import com.ahmer.accounts.ui.TransAddEditScreen
import com.ahmer.accounts.ui.TransListScreen

/**
 * Sets up the main navigation graph for the application.
 *
 * @param modifier Modifier to be applied to the navigation host.
 * @param navController Controller managing navigation between composable screens.
 * @param transactionSumModel Model containing transaction summary data.
 */
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    transactionSumModel: TransactionSumModel
) {
    NavHost(
        navController = navController,
        startDestination = NavItems.Accounts.fullRoute,
        modifier = modifier
    ) {
        addAccountsScreen(navController = navController, transactionSumModel = transactionSumModel)
        addReportScreen()
        addSettingsScreen()
        addPersonAddEditScreen(navController = navController)
        addTransactionsScreen(navController = navController)
        addTransactionsAddEditScreen(navController = navController)
    }
}

/**
 * Adds the accounts (persons list) screen to the navigation graph.
 *
 * @param navController NavHostController instance to handle navigation events.
 * @param transactionSumModel Model containing transaction summary data.
 */
private fun NavGraphBuilder.addAccountsScreen(
    navController: NavHostController, transactionSumModel: TransactionSumModel
) {
    composable(
        route = NavItems.Accounts.fullRoute,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        PersonsListScreen(
            onNavigation = { navController.navigate(it.route) },
            personViewModel = hiltViewModel(),
            settingsViewModel = hiltViewModel(),
            transactionSumModel = transactionSumModel
        )
    }
}

/**
 * Adds the report screen to the navigation graph.
 */
private fun NavGraphBuilder.addReportScreen() {
    composable(
        route = NavItems.Report.fullRoute,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        ReportScreen(mainViewModel = hiltViewModel(), viewModel = hiltViewModel())
    }
}

/**
 * Adds the settings screen to the navigation graph.
 */
private fun NavGraphBuilder.addSettingsScreen() {
    composable(
        route = NavItems.Settings.fullRoute,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        SettingsScreen(viewModel = hiltViewModel())
    }
}

/**
 * Adds the person add/edit screen to the navigation graph.
 *
 * @param navController NavHostController instance used for handling back navigation.
 */
private fun NavGraphBuilder.addPersonAddEditScreen(navController: NavHostController) {
    composable(
        route = NavItems.PersonAddEdit.fullRoute,
        arguments = listOf(
            navArgument(name = "personId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        PersonAddEditScreen(
            viewModel = hiltViewModel(), onPopBackStack = { navController.popBackStack() }
        )
    }
}

/**
 * Adds the transactions list screen to the navigation graph.
 *
 * @param navController NavHostController instance used for navigation.
 */
private fun NavGraphBuilder.addTransactionsScreen(navController: NavHostController) {
    composable(
        route = NavItems.Transactions.fullRoute,
        arguments = listOf(
            navArgument(name = "transPersonId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) { backStackEntry ->
        backStackEntry.arguments?.getInt("transPersonId")?.let { personId ->
            TransListScreen(
                onNavigation = { navController.navigate(it.route) },
                personId = personId,
                onPopBackStack = { navController.popBackStack() },
                personViewModel = hiltViewModel(),
                transViewModel = hiltViewModel(),
                settingsViewModel = hiltViewModel()
            )
        }
    }
}

/**
 * Adds the transactions add/edit screen to the navigation graph.
 *
 * @param navController NavHostController instance used for navigation.
 */
private fun NavGraphBuilder.addTransactionsAddEditScreen(navController: NavHostController) {
    composable(
        route = NavItems.TransactionsAddEdit.fullRoute,
        arguments = listOf(
            navArgument("transId") {
                type = NavType.IntType
                defaultValue = -1
            },
            navArgument("transPersonId") {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
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