package com.ahmer.accounts.ui

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.preferences.PreferencesFilter
import com.ahmer.accounts.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: PreferencesManager
) : ViewModel(), LifecycleObserver {

    val flow: Flow<PreferencesFilter> = preferences.preferencesFlow

    fun updateTheme(isChecked: Boolean) {
        viewModelScope.launch {
            preferences.updateAppTheme(isChecked)
        }
    }

    fun updateAppTheme(theme: Theme) {
        val mDefaultNightMode = when (theme) {
            Theme.Dark -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.Light -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.System -> {
                if (Build.VERSION.SDK_INT < 28) {
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
        }
        AppCompatDelegate.setDefaultNightMode(mDefaultNightMode)
    }
}