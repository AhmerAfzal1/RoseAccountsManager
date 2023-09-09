package com.ahmer.accounts.ui

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmer.accounts.R
import com.ahmer.accounts.drawer.DrawerItems
import com.ahmer.accounts.drawer.MenuSearchBar
import com.ahmer.accounts.drawer.NavShape
import com.ahmer.accounts.drawer.drawerItemsList
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.event.UserEvent
import com.ahmer.accounts.ui.components.UsersList
import com.ahmer.accounts.utils.AddIcon
import com.ahmer.accounts.utils.HelperFunctions
import com.ahmer.accounts.utils.MenuIcon
import com.ahmer.accounts.utils.SearchIcon
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.SortByDateIcon
import com.ahmer.accounts.utils.SortByNameIcon
import com.ahmer.accounts.utils.SortIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UsersListScreen(
    onNavigation: (UiEvent.Navigate) -> Unit
) {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mCredit = 10000.00
    val mDebit = 8000.00
    val mDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val mUserViewModel: UserViewModel = hiltViewModel()
    val mNavItemsList: List<DrawerItems> = drawerItemsList()
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val mSnackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    var mSelectedItems by rememberSaveable { mutableIntStateOf(0) }
    var mShowDropdownMenu by remember { mutableStateOf(false) }
    var mShowSearch by remember { mutableStateOf(false) }
    val mState by mUserViewModel.uiState.collectAsState()
    var mTextSearch by remember { mutableStateOf(mUserViewModel.searchQuery.value) }

    LaunchedEffect(key1 = true) {
        mUserViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigation(event)
                is UiEvent.ShowSnackBar -> {
                    val mResult = mSnackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                    if (mResult == SnackbarResult.ActionPerformed) {
                        mUserViewModel.onEvent(UserEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.ShowToast -> HelperFunctions.toastLong(mContext, event.message)
                else -> Unit
            }
        }
    }

    if (mShowSearch) {
        MenuSearchBar(text = mTextSearch, onTextChange = { mTextSearch = it }) {
            mShowSearch = false
        }/*CustomSearchView(
               text = mTextSearch,
               onTextChange = { mTextSearch = it },
               onCloseClick = {
                   mCoroutineScope.launch { delay(200L) }
               },
               onSearchClick = { mTextSearch = it }
           )*/
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavShape(mCredit, mDebit)
                Spacer(Modifier.height(12.dp))
                mNavItemsList.forEachIndexed { index, item ->
                    //Spacer(Modifier.height(6.dp))
                    NavigationDrawerItem(
                        label = { Text(text = item.label) },
                        selected = index == mSelectedItems,
                        onClick = {
                            //navController.navigate(item.route)
                            mSelectedItems = index
                            mCoroutineScope.launch { mDrawerState.close() }
                            HelperFunctions.toastLong(mContext, item.label)
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == mSelectedItems) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.contentDescription
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
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.app_name),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    mCoroutineScope.launch { mDrawerState.open() }
                }) { MenuIcon() }
            }, actions = {
                IconButton(onClick = { mShowSearch = true }) { SearchIcon() }
                IconButton(onClick = {
                    mShowDropdownMenu = !mShowDropdownMenu
                }) { SortIcon() }
                DropdownMenu(expanded = mShowDropdownMenu,
                    onDismissRequest = { mShowDropdownMenu = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_name)) },
                        onClick = {
                            mUserViewModel.onEvent(UserEvent.OnSortBy(SortBy.NAME))
                            mShowDropdownMenu = false
                        },
                        leadingIcon = { SortByNameIcon() })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_date_created)) },
                        onClick = {
                            mUserViewModel.onEvent(UserEvent.OnSortBy(SortBy.DATE))
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
        }, snackbarHost = { SnackbarHost(hostState = mSnackBarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    mUserViewModel.onEvent(UserEvent.OnNewAddClick)
                }) { AddIcon() }
            }) { innerPadding ->
            UsersList(
                padding = innerPadding,
                usersListState = mState.getAllUsersList,
                onEvent = mUserViewModel::onEvent,
                reloadData = mUserViewModel::getAllUsersData
            )
        }
    }
}