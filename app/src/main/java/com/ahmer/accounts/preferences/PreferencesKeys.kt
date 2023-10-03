package com.ahmer.accounts.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val APP_LOCK = booleanPreferencesKey(name = "AppLockKey")
    val APP_THEME = booleanPreferencesKey(name = "AppThemeKey")
    val SORT_ORDER = stringPreferencesKey(name = "SortOrderKey")
}