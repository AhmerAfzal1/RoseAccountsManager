package com.ahmer.accounts.preferences

import com.ahmer.accounts.utils.SortBy

data class PreferencesFilter(
    val appLock: Boolean,
    val appTheme: Boolean,
    val sortBy: SortBy
)