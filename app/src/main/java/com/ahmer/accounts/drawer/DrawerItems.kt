package com.ahmer.accounts.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R

data class DrawerItems(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String?,
    val badgeCount: Int? = null
)

@Composable
fun drawerItemsList(): List<DrawerItems> = listOf(
    DrawerItems(
        label = stringResource(R.string.label_home),
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        contentDescription = stringResource(id = R.string.content_description_home),
    ),
    DrawerItems(
        label = stringResource(R.string.label_backup),
        selectedIcon = Icons.Filled.Backup,
        unselectedIcon = Icons.Outlined.Backup,
        contentDescription = stringResource(id = R.string.content_description_backup),
    ),
    DrawerItems(
        label = stringResource(R.string.label_restore_backup),
        selectedIcon = Icons.Filled.SettingsBackupRestore,
        unselectedIcon = Icons.Outlined.SettingsBackupRestore,
        contentDescription = stringResource(id = R.string.content_description_restore_backup),
    ),
    DrawerItems(
        label = stringResource(R.string.label_settings),
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        contentDescription = stringResource(id = R.string.content_description_settings),
    ),
    DrawerItems(
        label = stringResource(R.string.label_rate_the_app),
        selectedIcon = Icons.Filled.StarRate,
        unselectedIcon = Icons.Outlined.StarRate,
        contentDescription = stringResource(id = R.string.content_description_rate_app),
    ),
    DrawerItems(
        label = stringResource(R.string.label_share_the_app),
        selectedIcon = Icons.Filled.Share,
        unselectedIcon = Icons.Outlined.Share,
        contentDescription = stringResource(id = R.string.content_description_share),
    ),
    DrawerItems(
        label = stringResource(R.string.label_more_apps),
        selectedIcon = Icons.Filled.Apps,
        unselectedIcon = Icons.Outlined.Apps,
        contentDescription = stringResource(id = R.string.content_description_more_app),
    ),
    DrawerItems(
        label = stringResource(R.string.label_exit),
        selectedIcon = Icons.Filled.ExitToApp,
        unselectedIcon = Icons.Outlined.ExitToApp,
        contentDescription = stringResource(id = R.string.content_description_exit),
    )
)