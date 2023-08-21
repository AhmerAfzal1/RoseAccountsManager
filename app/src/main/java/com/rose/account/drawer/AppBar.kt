package com.rose.account.drawer

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rose.account.R
import kotlinx.coroutines.launch

fun underProgressFeature(context: Context) {
    Toast.makeText(context, "This feature is under progress", Toast.LENGTH_LONG).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyTopAppBar(
    onNavIconClick: () -> Unit
) {
    val mContext = LocalContext.current.applicationContext
    var mShowSearch by remember { mutableStateOf(false) }
    var mTextSearch by remember { mutableStateOf("") }

    if (mShowSearch) {
        MenuSearchBar(
            query = mTextSearch,
            onQueryChange = { mTextSearch = it },
            onDismiss = { mShowSearch = false })
    }

    TopAppBar(title = {
        Text(
            text = stringResource(R.string.app_name),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    ), navigationIcon = {
        IconButton(onClick = {
            onNavIconClick()
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
        IconButton(onClick = { underProgressFeature(mContext) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sort),
                contentDescription = stringResource(id = R.string.content_description_sort)
            )
        }
    })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopAppBarWithNavigationBar() {
    val mContext = LocalContext.current.applicationContext
    val mCoroutineScope = rememberCoroutineScope()
    val mDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val mNavItemsList = drawerItemsList()
    val mSelectedItems = remember { mutableStateOf(mNavItemsList[0]) }
    val mCredit = 10000.00
    val mDebit = 8000.00

    ModalNavigationDrawer(drawerState = mDrawerState, drawerContent = {
        ModalDrawerSheet {
            NavShape(mCredit, mDebit)
            Spacer(Modifier.height(12.dp))
            mNavItemsList.forEach { item ->
                //Spacer(Modifier.height(6.dp))
                NavigationDrawerItem(
                    icon = { Icon(item.icon, contentDescription = item.contentDescription) },
                    label = { Text(item.label) },
                    selected = item == mSelectedItems.value,
                    onClick = {
                        mCoroutineScope.launch { mDrawerState.close() }
                        Toast.makeText(mContext, item.label, Toast.LENGTH_SHORT).show()
                        mSelectedItems.value = item
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }, content = {
        Scaffold(topBar = {
            MyTopAppBar { mCoroutineScope.launch { mDrawerState.open() } }
        }) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(text = "Text in Drawer content 1")
                    Text(text = "Text in Drawer content 2")
                }
            }
        }
    })
}
