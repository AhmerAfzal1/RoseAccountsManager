package com.ahmer.accounts.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ahmer.accounts.navigation.NavItems.Companion.Icons

/**
 * Extension function for [NavDestination] to determine if the destination's hierarchy
 * contains the given [route].
 *
 * @param route The route string to compare against the destination's hierarchy.
 * @return `true` if the destination belongs to the hierarchy for [route], `false` otherwise.
 */
private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } ?: false
}

/**
 * Composable displaying the bottom navigation bar with animated visibility.
 *
 * This bottom navigation bar will slide in/out vertically based on [isVisible] state.
 *
 * @param navController The [NavHostController] that handles navigation actions.
 * @param isVisible Flag controlling the visibility of the bottom navigation bar.
 */
@Composable
fun BottomNav(navController: NavHostController, isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(
            modifier = Modifier.navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 5.dp,
            windowInsets = WindowInsets.navigationBars,
        ) {
            val currentBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
            val currentDestination: NavDestination? = currentBackStackEntry?.destination

            NavItems.navBottomItems.forEach { navItem ->
                val isSelected = currentDestination.isRouteInHierarchy(route = navItem.route)

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(route = navItem.route) {
                            popUpTo(id = navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { navItem.Icons(selected = isSelected) },
                    label = {
                        Text(
                            text = stringResource(id = navItem.label),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}