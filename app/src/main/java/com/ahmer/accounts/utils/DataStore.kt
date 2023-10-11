package com.ahmer.accounts.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object DataStoreKeys {
        val sortOrderKey = stringPreferencesKey(name = Constants.DATA_STORE_KEY_SORT_ORDER)
        val themeKey = stringPreferencesKey(name = Constants.DATA_STORE_KEY_THEME)
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) = dataStore.edit { preference ->
        preference[DataStoreKeys.sortOrderKey] = sortOrder.name
    }

    suspend fun updateTheme(themeMode: ThemeMode) = dataStore.edit { preference ->
        preference[DataStoreKeys.themeKey] = themeMode.name
    }

    val getSortOrder: Flow<SortOrder> = dataStore.data.map { preference ->
        SortOrder.valueOf(value = preference[DataStoreKeys.sortOrderKey] ?: SortOrder.Date.name)
    }

    val getTheme: Flow<ThemeMode> = dataStore.data.map { preference ->
        ThemeMode.valueOf(value = preference[DataStoreKeys.themeKey] ?: ThemeMode.System.name)
    }
}