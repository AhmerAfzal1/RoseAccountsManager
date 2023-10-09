package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.utils.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel(), LifecycleObserver {
    val currentTheme: StateFlow<ThemeMode> = preferencesManager.getTheme().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = ThemeMode.System
    )

    fun updateTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            preferencesManager.updateTheme(themeMode = themeMode)
        }
    }
}