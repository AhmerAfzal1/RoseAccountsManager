package com.rose.account.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rose.account.R
import com.rose.account.database.model.UserModel
import com.rose.account.drawer.DrawerItems
import com.rose.account.drawer.NavShape
import com.rose.account.drawer.drawerItemsList
import com.rose.account.utils.SortOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun underProgressFeature(context: Context) {
    Toast.makeText(context, "This feature is under progress", Toast.LENGTH_LONG).show()
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier, userModel: UserModel, onDeleteClick: () -> Unit
) {
    val mIconSize: Dp = 36.dp
    val mPadding: Dp = 5.dp

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = mPadding, end = mPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = "${userModel.name}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(modifier = Modifier.then(Modifier.size(mIconSize)),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(id = R.string.content_description_info)
                    )
                }
                IconButton(modifier = Modifier.then(Modifier.size(mIconSize)),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.content_description_edit)
                    )
                }
                IconButton(modifier = Modifier.then(Modifier.size(mIconSize)),
                    onClick = { onDeleteClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.content_description_delete)
                    )
                }
            }
        }
        Column(
            modifier = Modifier.padding(start = mPadding, end = mPadding, bottom = mPadding)
        ) {
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = "Phone: ${userModel.phone}",
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopAppBarWithNavigationBar() {
    val mContext: Context = LocalContext.current.applicationContext
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mCredit: Double = 10000.00
    val mDebit: Double = 8000.00
    val mDrawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val mHomeViewModel: HomeViewModel = hiltViewModel()
    val mNavItemsList: List<DrawerItems> = drawerItemsList()
    val mScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var mSelectedItems by rememberSaveable { mutableIntStateOf(0) }
    var mShowDropdownMenu by remember { mutableStateOf(false) }
    var mShowSearch by remember { mutableStateOf(false) }
    var mTextSearch by remember { mutableStateOf("") }

    /*if (mShowSearch) {
        MenuSearchBar(
            query = mTextSearch,
            onQueryChange = { mTextSearch = it },
            onDismiss = { mShowSearch = false }
        )
    }*/

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
                            Toast.makeText(mContext, item.label, Toast.LENGTH_SHORT).show()
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
                            item.badgeCount?.let { Text(text = item.badgeCount.toString()) }
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
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(id = R.string.content_description_menu)
                    )
                }
            }, actions = {
                IconButton(onClick = { mShowSearch = true }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.content_description_search)
                    )
                }
                IconButton(onClick = { mShowDropdownMenu = !mShowDropdownMenu }) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = stringResource(id = R.string.content_description_sort)
                    )
                }
                DropdownMenu(expanded = mShowDropdownMenu,
                    onDismissRequest = { mShowDropdownMenu = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_name)) },
                        onClick = {
                            mHomeViewModel.updateSortOrder(SortOrder.BY_NAME)
                            mShowDropdownMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.SortByAlpha,
                                contentDescription = stringResource(id = R.string.content_description_sort_by_name)
                            )
                        })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.label_sort_by_date_created)) },
                        onClick = {
                            mHomeViewModel.updateSortOrder(SortOrder.BY_DATE)
                            mShowDropdownMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ChangeCircle,
                                contentDescription = stringResource(id = R.string.content_description_sort_by_date)
                            )
                        })
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ), scrollBehavior = mScrollBehavior
            )
        }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val mUsers by remember {
                    mHomeViewModel.getAllUsersWithFiltered
                }.collectAsStateWithLifecycle(initialValue = emptyList())

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mUsers) { user ->
                        UserItem(modifier = Modifier.fillMaxWidth(),
                            userModel = user,
                            onDeleteClick = { mHomeViewModel.userDelete(user) }
                        )
                    }
                }
            }
        }
    }
}