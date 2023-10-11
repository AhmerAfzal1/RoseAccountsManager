package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.DataStore
import com.ahmer.accounts.utils.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore,
) : ViewModel(), LifecycleObserver {
    val currentTheme: StateFlow<ThemeMode> = dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = Constants.STATE_IN_STARTED_TIME),
        initialValue = ThemeMode.System
    )

    fun updateTheme(themeMode: ThemeMode) {
        viewModelScope.launch {
            dataStore.updateTheme(themeMode = themeMode)
        }
    }
}