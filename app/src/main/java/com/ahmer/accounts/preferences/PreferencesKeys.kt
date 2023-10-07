package com.ahmer.accounts.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val APP_THEME = stringPreferencesKey(name = "AppThemeKey")
    val SORT_ORDER = stringPreferencesKey(name = "SortOrderKey")
}