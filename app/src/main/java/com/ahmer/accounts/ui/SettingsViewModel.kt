package com.ahmer.accounts.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmer.accounts.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: PreferencesManager
) : ViewModel(), LifecycleObserver {

    val flow = preferences.preferencesFlow

    fun updateTheme(isChecked: Boolean) {
        viewModelScope.launch {
            preferences.updateAppTheme(isChecked)
        }
    }
}