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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.dialogs.ThemeDialog
import com.ahmer.accounts.event.UiEvent
import com.ahmer.accounts.utils.AppVersion
import com.ahmer.accounts.utils.BackupIcon
import com.ahmer.accounts.utils.ClearCachesIcon
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.HelperUtils
import com.ahmer.accounts.utils.RestoreIcon
import com.ahmer.accounts.utils.ThemeIcon
import com.ahmer.accounts.utils.ThemeMode
import com.ahmer.accounts.utils.VersionIcon
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val mContext: Context = LocalContext.current.applicationContext
    val mAppVersion: AppVersion = HelperUtils.getAppInfo(context = mContext)
    val mCurrentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    val mSummary: String = ThemeMode.getThemeModesTitle(themeMode = mCurrentTheme)
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

    fun restore() {
        val mRestoreIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            type = "*/*"
            Intent.createChooser(this, mContext.getString(R.string.label_intent_chooser))
        }
        mRestoreDatabaseBackupLauncher.launch(mRestoreIntent)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            PreferenceCategory(title = stringResource(id = R.string.label_pref_category_theme)) {
                TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_theme)) },
                    summary = { Text(text = mSummary) },
                    icon = { ThemeIcon() },
                    onClick = { mShowThemeDialog = !mShowThemeDialog })
            }

            PreferenceCategory(title = stringResource(id = R.string.label_pref_category_backup_restore)) {
                TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_backup)) },
                    summary = { Text(text = "Backup your data manually") },
                    icon = { BackupIcon() },
                    onClick = { backup() })
                TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_restore)) },
                    summary = { Text(text = "Restore the backup") },
                    icon = { RestoreIcon() },
                    onClick = { restore() })
            }

            PreferenceCategory(title = stringResource(id = R.string.label_pref_category_general)) {
                TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_clear_caches)) },
                    summary = {
                        Text(
                            text = stringResource(
                                R.string.label_pref_text_summery_caches,
                                HelperUtils.getCacheSize(context = mContext)
                            )
                        )
                    },
                    icon = { ClearCachesIcon() },
                    onClick = {})
                TextPreference(title = { Text(text = stringResource(id = R.string.label_pref_text_title_app_version)) },
                    summary = { Text(text = "${mAppVersion.versionName} (${mAppVersion.versionCode})") },
                    icon = { VersionIcon() },
                    onClick = {})
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
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )
    }
}