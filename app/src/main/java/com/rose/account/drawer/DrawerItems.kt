package com.rose.account.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rose.account.R

data class DrawerItems(
    val icon: Painter,
    val label: String,
    val contentDescription: String?,
    val isSelected: Boolean
)

@Composable
fun drawerItemsList(): List<DrawerItems> = listOf(
    DrawerItems(
        painterResource(id = R.drawable.ic_home),
        label = stringResource(R.string.label_home),
        contentDescription = stringResource(id = R.string.content_description_home),
        isSelected = false
    ),

    DrawerItems(
        painterResource(id = R.drawable.ic_backup),
        label = stringResource(R.string.label_backup),
        contentDescription = stringResource(id = R.string.content_description_backup),
        isSelected = false
    ),
    DrawerItems(
        painterResource(id = R.drawable.ic_restore_backup),
        label = stringResource(R.string.label_restore_backup),
        contentDescription = stringResource(id = R.string.content_description_restore_backup),
        isSelected = false
    ),
    DrawerItems(
        painterResource(id = R.drawable.ic_settings),
        label = stringResource(R.string.label_settings),
        contentDescription = stringResource(id = R.string.content_description_settings),
        isSelected = false
    ),
    DrawerItems(
        painterResource(id = R.drawable.ic_star_rate),
        label = stringResource(R.string.label_rate_the_app),
        contentDescription = stringResource(id = R.string.content_description_rate_app),
        isSelected = false
    ),
    DrawerItems(
        painterResource(id = R.drawable.ic_share),
        label = stringResource(R.string.label_share_the_app),
        contentDescription = stringResource(id = R.string.content_description_share),
        isSelected = false
    ),
    DrawerItems(
        painterResource(id = R.drawable.ic_more_apps),
        label = stringResource(R.string.label_more_apps),
        contentDescription = stringResource(id = R.string.content_description_more_app),
        isSelected = false
    ),
    DrawerItems(
        painterResource(id = R.drawable.ic_exit),
        label = stringResource(R.string.label_exit),
        contentDescription = stringResource(id = R.string.content_description_exit),
        isSelected = false
    )
)