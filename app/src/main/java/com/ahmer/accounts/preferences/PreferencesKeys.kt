package com.ahmer.accounts.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val APP_LOCK = booleanPreferencesKey("AppLockKey")
    val APP_THEME = booleanPreferencesKey("AppThemeKey")
    val SORT_ORDER = stringPreferencesKey("SortOrderKey")
}