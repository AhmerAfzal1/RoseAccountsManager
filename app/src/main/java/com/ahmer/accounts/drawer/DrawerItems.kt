package com.ahmer.accounts.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R
import com.ahmer.accounts.navigation.NavRoutes

data class DrawerItems(
    val label: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val contentDescription: String?,
    val badgeCount: Int? = null,
    val route: String
)

@Composable
fun drawerItemsList(): List<DrawerItems> = listOf(
    DrawerItems(
        label = stringResource(R.string.label_home),
        selectedIcon = painterResource(id = R.drawable.ic_filled_home),
        unselectedIcon = painterResource(id = R.drawable.ic_outlined_home),
        contentDescription = stringResource(id = R.string.content_description_home),
        route = NavRoutes.Home,
    ),
    DrawerItems(
        label = stringResource(R.string.label_backup),
        selectedIcon = painterResource(id = R.drawable.ic_filled_backup),
        unselectedIcon = painterResource(id = R.drawable.ic_outlined_backup),
        contentDescription = stringResource(id = R.string.content_description_backup),
        route = NavRoutes.Backup,
    ),
    DrawerItems(
        label = stringResource(R.string.label_restore_backup),
        selectedIcon = painterResource(id = R.drawable.ic_filled_outlined_restore),
        unselectedIcon = painterResource(id = R.drawable.ic_filled_outlined_restore),
        contentDescription = stringResource(id = R.string.content_description_restore_backup),
        route = NavRoutes.Restore,
    ),
    DrawerItems(
        label = stringResource(R.string.label_settings),
        selectedIcon = painterResource(id = R.drawable.ic_filled_settings),
        unselectedIcon = painterResource(id = R.drawable.ic_outlined_settings),
        contentDescription = stringResource(id = R.string.content_description_settings),
        route = NavRoutes.Settings,
    ),
    DrawerItems(
        label = stringResource(R.string.label_rate_the_app),
        selectedIcon = painterResource(id = R.drawable.ic_filled_star),
        unselectedIcon = painterResource(id = R.drawable.ic_outlined_star),
        contentDescription = stringResource(id = R.string.content_description_rate_app),
        route = NavRoutes.Rate,
    ),
    DrawerItems(
        label = stringResource(R.string.label_share_the_app),
        selectedIcon = painterResource(id = R.drawable.ic_filled_share),
        unselectedIcon = painterResource(id = R.drawable.ic_outlined_share),
        contentDescription = stringResource(id = R.string.content_description_share),
        route = NavRoutes.Share,
    ),
    DrawerItems(
        label = stringResource(R.string.label_more_apps),
        selectedIcon = painterResource(id = R.drawable.ic_filled_outlined_apps),
        unselectedIcon = painterResource(id = R.drawable.ic_filled_outlined_apps),
        contentDescription = stringResource(id = R.string.content_description_more_app),
        route = NavRoutes.MoreApp,
    ),
    DrawerItems(
        label = stringResource(R.string.label_exit),
        selectedIcon = painterResource(id = R.drawable.ic_filled_outlined_exit),
        unselectedIcon = painterResource(id = R.drawable.ic_filled_outlined_exit),
        contentDescription = stringResource(id = R.string.content_description_exit),
        route = NavRoutes.Exit,
    )
)