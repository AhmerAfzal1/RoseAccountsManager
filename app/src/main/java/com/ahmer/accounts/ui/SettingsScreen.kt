package com.ahmer.accounts.ui

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.dialogs.ThemeDialog
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.utils.AppVersion
import com.ahmer.accounts.utils.BackupIcon
import com.ahmer.accounts.utils.CheckIcon
import com.ahmer.accounts.utils.ClearCachesIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.Currency
import com.ahmer.accounts.utils.CurrencyIcon
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.RestoreIcon
import com.ahmer.accounts.utils.ThemeIcon
import com.ahmer.accounts.utils.ThemeMode
import com.ahmer.accounts.utils.VersionIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val mContext: Context = LocalContext.current.applicationContext
    val mAppVersion: AppVersion = HelperUtils.appInfo(context = mContext)
    val mCurrentCurrency: Currency by viewModel.currentCurrency.collectAsStateWithLifecycle()
    val mCurrentTheme: ThemeMode by viewModel.currentTheme.collectAsStateWithLifecycle()
    val mShowBottomSheet: MutableState<Boolean> = remember { mutableStateOf(value = false) }
    val mSummary: String = ThemeMode.getDisplayTitle(themeMode = mCurrentTheme)
    var mShowThemeDialog: Boolean by remember { mutableStateOf(value = false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.RelaunchApp -> HelperUtils.relaunchApp(context = mContext)
                is UiEvent.ShowToast -> HelperUtils.showToast(
                    context = mContext, msg = event.message
                )

                else -> Unit
            }
        }
    }

    if (mShowThemeDialog) {
        ThemeDialog(viewModel = viewModel)
    }

    if (mShowBottomSheet.value) {
        CurrencySelectionModal(
            viewModel = viewModel, showBottomSheet = mShowBottomSheet, currency = mCurrentCurrency
        )
    }

    val mBackupDatabaseLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val mUri = result.data?.data ?: return@rememberLauncherForActivityResult
            viewModel.backupDatabase(context = mContext, uri = mUri)
        }
    }

    val mRestoreDatabaseBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val mUri = result.data?.data ?: return@rememberLauncherForActivityResult
            viewModel.restoreDatabase(context = mContext, uri = mUri)
        }
    }

    fun backup() {
        val mFileName = "backup_${
            HelperUtils.getDateTime(
                time = System.currentTimeMillis(), pattern = Constants.PATTERN_FILE_NAME
            )
        }.db"
        val mBackupIntent: Intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            val mMimeType = "application/octet-stream"
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(mMimeType))
            putExtra(Intent.EXTRA_TITLE, mFileName)
            type = mMimeType
        }
        mBackupDatabaseLauncher.launch(mBackupIntent)
    }

    fun restore() {
        val mRestoreIntent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            type = "*/*"
            Intent.createChooser(this, mContext.getString(R.string.label_intent_chooser))
        }
        mRestoreDatabaseBackupLauncher.launch(mRestoreIntent)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PreferenceCategory(title = stringResource(id = R.string.label_pref_category_theme)) {
                TextPreference(
                    title = { Text(text = stringResource(id = R.string.label_pref_text_title_theme)) },
                    summary = { Text(text = mSummary) },
                    icon = { ThemeIcon() },
                    onClick = { mShowThemeDialog = !mShowThemeDialog })
            }

            PreferenceCategory(title = stringResource(id = R.string.label_pref_category_backup_restore)) {
                TextPreference(
                    title = { Text(text = stringResource(id = R.string.label_pref_text_title_backup)) },
                    summary = { Text(text = "Backup your data manually") },
                    icon = { BackupIcon() },
                    onClick = { backup() })
                TextPreference(
                    title = { Text(text = stringResource(id = R.string.label_pref_text_title_restore)) },
                    summary = { Text(text = "Restore the backup") },
                    icon = { RestoreIcon() },
                    onClick = { restore() })
            }

            PreferenceCategory(title = stringResource(id = R.string.label_pref_category_general)) {
                TextPreference(
                    title = { Text(text = stringResource(id = R.string.label_pref_text_title_currency)) },
                    summary = { Text(text = mCurrentCurrency.code) },
                    icon = { CurrencyIcon() },
                    onClick = { mShowBottomSheet.value = !mShowBottomSheet.value })
                TextPreference(
                    title = { Text(text = stringResource(id = R.string.label_pref_text_title_clear_caches)) },
                    summary = {
                        Text(
                            text = stringResource(
                                id = R.string.label_pref_text_summery_caches,
                                HelperUtils.cacheSize(context = mContext)
                            )
                        )
                    },
                    icon = { ClearCachesIcon() },
                    onClick = {})
                TextPreference(
                    title = { Text(text = stringResource(id = R.string.label_pref_text_title_app_version)) },
                    summary = { Text(text = "${mAppVersion.versionName} (${mAppVersion.versionCode})") },
                    icon = { VersionIcon() },
                    onClick = { })
            }
        }
    }
}

@Composable
fun TextPreference(
    title: @Composable (() -> Unit),
    summary: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    controls: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp)
            .alpha(if (enabled) 1f else 0.4f),
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .padding(start = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) { icon() }
        } else {
            Box(modifier = Modifier.size(0.dp))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.titleMedium) { title() }
            if (summary != null) {
                Spacer(modifier = Modifier.height(2.dp))
                ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) { summary() }
            }
        }
        if (controls != null) {
            Box(modifier = Modifier.padding(start = 24.dp)) { controls() }
        }
    }
}

@Composable
fun PreferenceCategory(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Row(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
            Text(
                modifier = Modifier.padding(start = 32.dp),
                text = title,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
        content()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 0.5.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionModal(
    viewModel: SettingsViewModel, showBottomSheet: MutableState<Boolean>, currency: Currency
) {
    val mCoroutineScope: CoroutineScope = rememberCoroutineScope()
    val mCurrencyList: List<Currency> = Currency.listOfCurrencies
    val mLazyListState: LazyListState = rememberLazyListState()
    val mSheetState: SheetState = rememberModalBottomSheetState()
    var isSelected: Boolean by remember { mutableStateOf(value = false) }
    var mSelectedCurrency: Currency by remember { mutableStateOf(value = currency) }

    LaunchedEffect(key1 = mSelectedCurrency) {
        val mIndex: Int = mCurrencyList.indexOf(mSelectedCurrency)
        val mOffset: Int = -mLazyListState.layoutInfo.viewportEndOffset / 2
        if (mIndex != -1) {
            mCoroutineScope.launch {
                mLazyListState.animateScrollToItem(index = mIndex, scrollOffset = mOffset)
            }
        }
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            modifier = Modifier.fillMaxHeight(fraction = 0.5f),
            sheetState = mSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }) {
            Text(
                text = stringResource(R.string.label_bottom_sheet_select_currency),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
            LazyColumn(
                modifier = Modifier,
                state = mLazyListState
            ) {
                items(items = mCurrencyList) { currency ->
                    isSelected = currency == mSelectedCurrency
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                mSelectedCurrency = currency
                                viewModel.updateCurrency(currency = currency)
                            }
                            .padding(all = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currency.flag,
                            modifier = Modifier
                                .weight(weight = 0.45f)
                                .padding(start = 5.dp),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = currency.code,
                            modifier = Modifier.weight(weight = 0.45f),
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.weight(weight = 0.1f))
                        if (isSelected) {
                            CheckIcon(modifier = Modifier, tint = Color.Blue)
                        } else {
                            Spacer(modifier = Modifier.size(size = 24.dp))
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 5.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}