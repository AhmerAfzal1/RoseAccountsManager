package com.ahmer.accounts.drawer

import com.ahmer.accounts.R
import com.ahmer.accounts.navigation.NavRoutes

sealed class DrawerItems(
    val label: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val contentDescription: Int,
    val badgeCount: Int? = null,
    val route: String
) {
    data object Home : DrawerItems(
        label = R.string.label_home,
        selectedIcon = R.drawable.ic_filled_home,
        unselectedIcon = R.drawable.ic_outlined_home,
        contentDescription = R.string.content_description_home,
        route = NavRoutes.Home
    )

    data object Backup : DrawerItems(
        label = R.string.label_backup,
        selectedIcon = R.drawable.ic_filled_backup,
        unselectedIcon = R.drawable.ic_outlined_backup,
        contentDescription = R.string.content_description_backup,
        route = NavRoutes.Backup
    )

    data object Restore : DrawerItems(
        label = R.string.label_restore_backup,
        selectedIcon = R.drawable.ic_filled_outlined_restore,
        unselectedIcon = R.drawable.ic_filled_outlined_restore,
        contentDescription = R.string.content_description_restore_backup,
        route = NavRoutes.Restore
    )

    data object Settings : DrawerItems(
        label = R.string.label_settings,
        selectedIcon = R.drawable.ic_filled_settings,
        unselectedIcon = R.drawable.ic_outlined_settings,
        contentDescription = R.string.content_description_settings,
        route = NavRoutes.Settings
    )

    data object Rate : DrawerItems(
        label = R.string.label_rate_the_app,
        selectedIcon = R.drawable.ic_filled_star,
        unselectedIcon = R.drawable.ic_outlined_star,
        contentDescription = R.string.content_description_rate_app,
        route = NavRoutes.Rate
    )

    data object Share : DrawerItems(
        label = R.string.label_share_the_app,
        selectedIcon = R.drawable.ic_filled_share,
        unselectedIcon = R.drawable.ic_outlined_share,
        contentDescription = R.string.content_description_share,
        route = NavRoutes.Share
    )

    data object MoreApps : DrawerItems(
        label = R.string.label_more_apps,
        selectedIcon = R.drawable.ic_filled_outlined_apps,
        unselectedIcon = R.drawable.ic_filled_outlined_apps,
        contentDescription = R.string.content_description_more_app,
        route = NavRoutes.MoreApp
    )

    data object Exit : DrawerItems(
        label = R.string.label_exit,
        selectedIcon = R.drawable.ic_filled_outlined_exit,
        unselectedIcon = R.drawable.ic_filled_outlined_exit,
        contentDescription = R.string.content_description_exit,
        route = NavRoutes.Exit
    )


    companion object {
        val listOfDrawerItems: List<DrawerItems> = listOf(
            Home, Backup, Restore, Settings, Rate, Share, MoreApps, Exit
        )
    }
}


