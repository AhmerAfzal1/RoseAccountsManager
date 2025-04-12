package com.ahmer.accounts.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ahmer.accounts.navigation.BottomNav
import com.ahmer.accounts.navigation.MainNavigation
import com.ahmer.accounts.navigation.NavItems
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.theme.RoseAccountsManagerTheme
import com.ahmer.accounts.utils.ThemeMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { viewModel.isLoadingSplash.value }
        setContent {
            val currentTheme: ThemeMode by settingsViewModel.currentTheme.collectAsStateWithLifecycle()
            val isDarkTheme: Boolean = when (currentTheme) {
                ThemeMode.Dark -> true
                ThemeMode.Light -> false
                ThemeMode.System -> isSystemInDarkTheme()
            }
            RoseAccountsManagerTheme(darkTheme = isDarkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

/**
 * Main screen composable containing the navigation scaffold and bottom bar
 *
 * @param viewModel ViewModel instance providing the UI state
 */
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController: NavHostController = rememberNavController()
    val currentState: MainState by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomBarVisible = rememberBottomBarVisibility(navController = navController)

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        bottomBar = { BottomNav(navController = navController, isVisible = bottomBarVisible) }
    ) { innerPadding ->
        MainNavigation(
            modifier = Modifier.padding(paddingValues = innerPadding),
            navController = navController,
            transactionSumModel = currentState.accountsBalance
        )
    }
}

/**
 * Determines bottom bar visibility based on current navigation route
 *
 * @param navController Navigation controller for observing route changes
 * @return Boolean indicating if bottom bar should be visible
 */
@Composable
private fun rememberBottomBarVisibility(navController: NavHostController): Boolean {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    return remember(navBackStackEntry) {
        derivedStateOf {
            val route = navBackStackEntry?.destination?.route
            route !in setOf(
                NavItems.PersonAddEdit.fullRoute,
                NavItems.Transactions.fullRoute,
                NavItems.TransactionsAddEdit.fullRoute
            )
        }
    }.value
}