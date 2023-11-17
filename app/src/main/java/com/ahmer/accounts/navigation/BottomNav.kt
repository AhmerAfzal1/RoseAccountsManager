package com.ahmer.accounts.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

@Composable
fun BottomNav(navController: NavHostController, bottomBarState: Boolean) {
    var mSelectedItem: Int by rememberSaveable { mutableIntStateOf(value = 0) }

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(
            modifier = Modifier.navigationBarsPadding(),
            tonalElevation = 5.dp,
            windowInsets = WindowInsets.navigationBars,
        ) {
            val mNavBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
            val mCurrentDestination: NavDestination? = mNavBackStackEntry?.destination

            NavItems.navBottomItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = mCurrentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        mSelectedItem = index
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { item.Icons(selected = mSelectedItem == index) },
                    label = {
                        Text(
                            text = stringResource(id = item.label),
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