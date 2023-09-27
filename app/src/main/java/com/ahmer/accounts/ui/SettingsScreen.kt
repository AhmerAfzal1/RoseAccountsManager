package com.ahmer.accounts.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.utils.BackIcon
import com.ahmer.accounts.utils.ClearCachesIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.ThemeIcon
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onPopBackStack: () -> Unit) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: SettingsViewModel = hiltViewModel()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(R.string.label_settings),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { onPopBackStack() }) { BackIcon() }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ), scrollBehavior = mScrollBehavior
        )
    }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) }) { innerPadding ->

        val themes = remember {
            listOf(
                Theme.Dark to "Dark",
                Theme.Light to "Light",
                Theme.System to "System Default",
            )
        }

        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadingPreference(headingText = "Theme", modifier = Modifier.fillMaxWidth())
            TitlePreference(
                titleText = "Change Theme",
                subTitleText = "System",
                icon = { ThemeIcon() },
                modifier = Modifier.fillMaxWidth()
            )

            HeadingPreference(headingText = "General", modifier = Modifier.fillMaxWidth())
            TitlePreference(
                titleText = "Clear Caches",
                subTitleText = "Caches Size: ${HelperUtils.getCacheSize(mContext)}",
                icon = { ClearCachesIcon() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TitlePreference(
    titleText: String,
    subTitleText: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Column(
            modifier = modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleText,
                modifier = modifier.padding(top = 5.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = subTitleText,
                modifier = modifier,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun HeadingPreference(headingText: String, modifier: Modifier = Modifier) {
    Text(
        text = headingText,
        modifier = modifier.padding(start = 16.dp, top = 16.dp, bottom = 5.dp),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

object Theme {
    const val Dark = "dark"
    const val Light = "light"
    const val System = "system"
}