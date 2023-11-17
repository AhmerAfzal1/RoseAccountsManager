package com.ahmer.accounts.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmer.accounts.R
import com.ahmer.accounts.ui.SettingsViewModel
import com.ahmer.accounts.utils.ThemeMode

@Composable
fun ThemeDialog(viewModel: SettingsViewModel) {
    val mCurrentTheme: ThemeMode by viewModel.currentTheme.collectAsStateWithLifecycle()
    val mListThemeModes: List<Pair<ThemeMode, String>> = ThemeMode.listOfThemeModes
    val (themeMode, setThemeMode) = remember { mutableStateOf(value = mCurrentTheme) }
    var mOpenDialog: Boolean by remember { mutableStateOf(value = true) }

    if (mOpenDialog) {
        AlertDialog(
            onDismissRequest = { mOpenDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.label_choose_theme),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }, text = {
                Column(
                    modifier = Modifier.selectableGroup(),
                    verticalArrangement = Arrangement.Center,
                    content = {
                        mListThemeModes.forEach { (option, title) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = themeMode.name == option.name,
                                        role = Role.RadioButton,
                                        onClick = { setThemeMode(option) },
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (themeMode.name == option.name),
                                    onClick = { setThemeMode(option) }
                                )
                                Text(text = title)
                            }
                        }
                    }
                )
            }, confirmButton = {
                TextButton(onClick = {
                    mOpenDialog = false
                    viewModel.updateTheme(themeMode = themeMode)
                }) {
                    Text(text = stringResource(id = R.string.label_ok), fontSize = 14.sp)
                }
            }, dismissButton = {
                TextButton(onClick = { mOpenDialog = false }) {
                    Text(text = stringResource(id = R.string.label_cancel), fontSize = 14.sp)
                }
            }
        )
    }
}