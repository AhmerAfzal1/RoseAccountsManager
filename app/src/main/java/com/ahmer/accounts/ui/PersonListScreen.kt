package com.ahmer.accounts.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.drawer.DrawerItems
import com.ahmer.accounts.drawer.NavShape
import com.ahmer.accounts.drawer.TopAppBarSearchBox
import com.ahmer.accounts.event.PersonEvent
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.navigation.NavRoutes
import com.ahmer.accounts.navigation.ScreenRoutes
import com.ahmer.accounts.ui.components.PersonsList
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.MenuIcon
import com.ahmer.accounts.utils.SearchIcon
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.SortByDateIcon
import com.ahmer.accounts.utils.SortByNameIcon
import com.ahmer.accounts.utils.SortIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonsListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit
) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val mNavItemsList: List<DrawerItems> = DrawerItems.listOfDrawerItems
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val mViewModel: PersonViewModel = hiltViewModel()
    val mState by mViewModel.uiState.collectAsState()
    var mSelectedItems by rememberSaveable { mutableIntStateOf(0) }
    var mShowDropdownMenu by remember { mutableStateOf(false) }
    var mShowSearch by remember { mutableStateOf(false) }
    var mTextSearch by remember { mutableStateOf(mViewModel.searchQuery.value) }

    val mBackupDatabaseLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mUri = result.data?.data ?: return@rememberLauncherForActivityResult
            mViewModel.backupDatabase(mContext, mUri)
        }
    }

    val mRestoreDatabaseBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mUri = result.data?.data ?: return@rememberLauncherForActivityResult
            mViewModel.restoreDatabase(mContext, mUri)
        }
    }

    LaunchedEffect(key1 = true) {
        mViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (mResult == SnackbarResult.ActionPerformed) {
                        mViewModel.onEvent(PersonEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.RelaunchApp -> HelperUtils.relaunchApp(mContext)
                is UiEvent.ShowToast -> HelperUtils.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    fun navController(route: String) {
        when (route) {
            NavRoutes.Home -> {
                onNavigation(UiEvent.Navigate(ScreenRoutes.PersonListScreen))
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
                    Intent.createChooser(this, "Select the database backup file")
                }
                mRestoreDatabaseBackupLauncher.launch(mRestoreIntent)
            }

            NavRoutes.Settings -> {
                onNavigation(UiEvent.Navigate(ScreenRoutes.SettingsScreen))
            }

            NavRoutes.Rate -> {
                HelperUtils.runWeb(context = mContext, packageName = mContext.packageName)
            }

            NavRoutes.Share -> {
                HelperUtils.shareApp(context = mContext)
            }

            NavRoutes.MoreApp -> {
                HelperUtils.moreApps(context = mContext)
            }

            NavRoutes.Exit -> {
                mViewModel.closeDatabase(mContext)
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
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    if (mShowSearch) {
                        TopAppBarSearchBox(text = mTextSearch, onTextChange = {
                            mViewModel.onEvent(PersonEvent.OnSearchTextChange(it))
                            mTextSearch = it
                        }, onCloseClick = {
                            mCoroutineScope.launch { delay(200.milliseconds) }
                            mShowSearch = false
                        })
                    } else {
                        Text(
                            text = stringResource(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }, navigationIcon = {
                    IconButton(onClick = {
                        mCoroutineScope.launch { mDrawerState.open() }
                    }) { MenuIcon() }
                }, actions = {
                    if (!mShowSearch) {
                        IconButton(onClick = { mShowSearch = true }) { SearchIcon() }
                    }
                    IconButton(onClick = {
                        mShowDropdownMenu = !mShowDropdownMenu
                    }) { SortIcon() }
                    DropdownMenu(expanded = mShowDropdownMenu,
                        onDismissRequest = { mShowDropdownMenu = false }) {
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_name)) },
                            onClick = {
                                mViewModel.onEvent(PersonEvent.OnSortBy(SortBy.NAME))
                                mShowDropdownMenu = false
                            },
                            leadingIcon = { SortByNameIcon() })
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_date_created)) },
                            onClick = {
                                mViewModel.onEvent(PersonEvent.OnSortBy(SortBy.DATE))
                                mShowDropdownMenu = false
                            },
                            leadingIcon = { SortByDateIcon() })
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ), scrollBehavior = mScrollBehavior
                )
            },
            snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    mViewModel.onEvent(PersonEvent.OnNewAddClick)
                }) { AddIcon() }
            }) { innerPadding ->
            PersonsList(
                padding = innerPadding,
                personsListState = mState.getAllPersonsList,
                onEvent = mViewModel::onEvent,
                reloadData = mViewModel::getAllPersonsData
            )
        }
    }
}