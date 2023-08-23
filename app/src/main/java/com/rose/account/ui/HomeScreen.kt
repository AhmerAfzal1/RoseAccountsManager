package com.rose.account.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rose.account.R
import com.rose.account.database.model.UsersModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val mHomeViewModel: HomeViewModel = hiltViewModel()

    val mUsers by remember {
        mHomeViewModel.getAllUsers()
    }.collectAsStateWithLifecycle(initialValue = emptyList())

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mUsers) { user ->
            UserItem(modifier = Modifier.fillMaxWidth(), users = user)
        }
    }
}

@Composable
private fun UserItem(modifier: Modifier = Modifier, users: UsersModel) {
    ElevatedCard(
        modifier = modifier, shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        val mIconSize: Dp = 36.dp
        val mPadding: Dp = 5.dp
        Row(
            modifier = Modifier
                .padding(start = mPadding, end = mPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = mPadding),
                text = "${users.name}",
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
                IconButton(
                    modifier = Modifier.then(Modifier.size(mIconSize)),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.content_description_edit)
                    )
                }
                IconButton(
                    modifier = Modifier.then(Modifier.size(mIconSize)),
                    onClick = { /*TODO*/ }) {
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
                text = "Phone: ${users.phone}",
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}