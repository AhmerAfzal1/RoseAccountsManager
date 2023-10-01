package com.ahmer.accounts.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ahmer.accounts.R
import com.ahmer.accounts.drawer.DrawerItems
import com.ahmer.accounts.drawer.NavShape
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.MainNavigation
import com.ahmer.accounts.navigation.NavRoutes
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.state.AppBarState
import com.ahmer.accounts.state.MainState
import com.ahmer.accounts.ui.theme.RoseAccountsManagerTheme
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MenuIcon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { mViewModel.isLoadingSplash.value }
        }
        setContent {
            RoseAccountsManagerTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = mViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val mNavItemsList: List<DrawerItems> = DrawerItems.listOfDrawerItems
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mState: MainState by viewModel.uiState.collectAsState()
    var mSelectedItems: Int by rememberSaveable { mutableIntStateOf(0) }
    val mNavHostController: NavHostController = rememberNavController()

    val mBackupDatabaseLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val mUri = result.data?.data ?: return@rememberLauncherForActivityResult
            viewModel.backupDatabase(mContext, mUri)
        }
    }

    val mRestoreDatabaseBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val mUri = result.data?.data ?: return@rememberLauncherForActivityResult
            viewModel.restoreDatabase(mContext, mUri)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.RelaunchApp -> HelperUtils.relaunchApp(mContext)
                is UiEvent.ShowToast -> HelperUtils.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    fun navController(route: String) {
        when (route) {
            NavRoutes.Home -> {
                mNavHostController.navigate(ScreenRoutes.PersonListScreen)
            }

            NavRoutes.Backup -> {
                val mFileName = "backup_${
                    HelperUtils.getDateTime(
                        time = System.currentTimeMillis(),
                        pattern = Constants.DATE_TIME_FILE_NAME_PATTERN
                    )
                }.db"
                val mBackupIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    val mMimeType = "application/octet-stream"
                    addCategory(Intent.CATEGORY_OPENABLE)
                    flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(mMimeType))
                    putExtra(Intent.EXTRA_TITLE, mFileName)
                    type = mMimeType
                }
                mBackupDatabaseLauncher.launch(mBackupIntent)
            }

            NavRoutes.Restore -> {
                val mRestoreIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    type = "*/*"
                    Intent.createChooser(this, mContext.getString(R.string.label_intent_chooser))
                }
                mRestoreDatabaseBackupLauncher.launch(mRestoreIntent)
            }

            NavRoutes.Settings -> {
                mNavHostController.navigate(ScreenRoutes.SettingsScreen)
            }

            NavRoutes.Rate -> {
                HelperUtils.runWeb(
                    context = mContext, packageName = mContext.packageName
                )
            }

            NavRoutes.Share -> {
                HelperUtils.shareApp(context = mContext)
            }

            NavRoutes.MoreApp -> {
                HelperUtils.moreApps(context = mContext)
            }

            NavRoutes.Exit -> {
                viewModel.closeDatabase(mContext)
                MainActivity().finish()
                exitProcess(0)
            }
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavShape(mState.getAllPersonsBalance)
                Spacer(Modifier.height(12.dp))
                mNavItemsList.forEachIndexed { index, item ->
                    //Spacer(Modifier.height(6.dp))
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(id = item.label)) },
                        selected = index == mSelectedItems,
                        onClick = {
                            mSelectedItems = index
                            navController(route = item.route)
                            mCoroutineScope.launch { mDrawerState.close() }
                        },
                        icon = {
                            Icon(
                                painter = if (index == mSelectedItems) {
                                    painterResource(id = item.selectedIcon)
                                } else painterResource(id = item.unselectedIcon),
                                contentDescription = stringResource(id = item.contentDescription)
                            )
                        },
                        badge = {
                            item.badgeCount?.let { Text(text = item.run { badgeCount.toString() }) }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }, drawerState = mDrawerState
    ) {
        var mAppBarState by remember { mutableStateOf(AppBarState()) }

        Scaffold(modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = { mAppBarState.searchActions() },
                    navigationIcon = {
                        if (mAppBarState.isMenuNavigationIcon) {
                            IconButton(onClick = { mCoroutineScope.launch { mDrawerState.open() } }) {
                                MenuIcon()
                            }
                        } else mAppBarState.newNavigationIcon()

                    },
                    actions = { mAppBarState.actions(this) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ), scrollBehavior = mScrollBehavior
                )
            },
            snackbarHost = {
                if (!mAppBarState.isSnackBarRequired) {
                    SnackbarHost(hostState = mSnackBarHostState)
                } else mAppBarState.newSnackBarHost()

            },
            floatingActionButton = { mAppBarState.floatingAction() }
        ) { innerPadding ->
            MainNavigation(
                navHostController = mNavHostController,
                appBarState = { mAppBarState = it },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}