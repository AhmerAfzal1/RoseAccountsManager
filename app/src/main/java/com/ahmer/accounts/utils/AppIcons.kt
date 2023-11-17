package com.ahmer.accounts.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ahmer.accounts.R

@Composable
fun AddCircleIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_add_circle_outline),
        contentDescription = stringResource(id = R.string.content_description_add),
        modifier = modifier,
        tint = tint
    )
}

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
fun AddressIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = stringResource(id = R.string.content_description_address),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun BackIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
        contentDescription = stringResource(id = R.string.content_description_back),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun BackupIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_filled_backup),
        contentDescription = stringResource(id = R.string.content_description_backup),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun CheckIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Check,
        contentDescription = stringResource(id = R.string.content_description_check),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun ClearCachesIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_clear_cached),
        contentDescription = stringResource(id = R.string.content_description_clear_cached),
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
        painter = painterResource(id = R.drawable.ic_credit),
        contentDescription = stringResource(id = R.string.content_description_credit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun CurrencyIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_dollar),
        contentDescription = stringResource(id = R.string.content_description_dollar),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun DateIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.DateRange,
        contentDescription = stringResource(id = R.string.content_description_date),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun DebitIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_debit),
        contentDescription = stringResource(id = R.string.content_description_debit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun DeleteIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_delete_forever),
        contentDescription = stringResource(id = R.string.content_description_delete),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun EditIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Outlined.Edit,
        contentDescription = stringResource(id = R.string.content_description_edit),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun EmailIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Outlined.Email,
        contentDescription = stringResource(id = R.string.content_description_email),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun InfoIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Outlined.Info,
        contentDescription = stringResource(id = R.string.content_description_info),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun MoreIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = stringResource(id = R.string.content_description_more),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun NotesIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_notes),
        contentDescription = stringResource(id = R.string.content_description_notes),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun PdfIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_pdf),
        contentDescription = stringResource(id = R.string.content_description_pdf),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun PersonIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Outlined.Person,
        contentDescription = stringResource(id = R.string.content_description_person),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun PhoneIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Outlined.Phone,
        contentDescription = stringResource(id = R.string.content_description_phone),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun RestoreIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_filled_outlined_restore),
        contentDescription = stringResource(id = R.string.content_description_restore_backup),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SaveIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_save),
        contentDescription = stringResource(id = R.string.content_description_save),
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
fun SettingsIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        imageVector = Icons.Filled.Settings,
        contentDescription = stringResource(id = R.string.content_description_settings),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortByDateIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_access_time),
        contentDescription = stringResource(id = R.string.content_description_sort_by_date),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortByNameIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_sort_by_alpha),
        contentDescription = stringResource(id = R.string.content_description_sort_by_name),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun SortIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_sort),
        contentDescription = stringResource(id = R.string.content_description_sort),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun ThemeIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_dark_mode),
        contentDescription = stringResource(id = R.string.content_description_theme),
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun VersionIcon(modifier: Modifier = Modifier, tint: Color = LocalContentColor.current) {
    Icon(
        painter = painterResource(id = R.drawable.ic_version),
        contentDescription = stringResource(id = R.string.content_description_version),
        modifier = modifier,
        tint = tint
    )
}