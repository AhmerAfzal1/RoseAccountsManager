package com.ahmer.accounts.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R

@Composable
fun AddIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Add,
        contentDescription = stringResource(id = R.string.content_description_add),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun BackIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = stringResource(id = R.string.content_description_back),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Close,
        contentDescription = stringResource(id = R.string.content_description_close),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun CreditIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.AddCircleOutline,
        contentDescription = stringResource(id = R.string.content_description_credit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun DebitIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.RemoveCircleOutline,
        contentDescription = stringResource(id = R.string.content_description_debit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun DeleteIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.DeleteForever,
        contentDescription = stringResource(id = R.string.content_description_delete),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SaveIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Save,
        contentDescription = stringResource(id = R.string.content_description_save),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun EditIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_edit_square),
        contentDescription = stringResource(id = R.string.content_description_edit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun InfoIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Info,
        contentDescription = stringResource(id = R.string.content_description_info),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun MenuIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Menu,
        contentDescription = stringResource(id = R.string.content_description_menu),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SearchIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Search,
        contentDescription = stringResource(id = R.string.content_description_search),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortByDateIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.AccessTime,
        contentDescription = stringResource(id = R.string.content_description_sort_by_date),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortByNameIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.SortByAlpha,
        contentDescription = stringResource(id = R.string.content_description_sort_by_name),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Sort,
        contentDescription = stringResource(id = R.string.content_description_sort),
        modifier = modifier,
        tint = tint
    )
}