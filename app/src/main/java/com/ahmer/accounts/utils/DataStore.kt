package com.ahmer.accounts.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Utility class for managing app preferences using DataStore.
 */
class DataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object DataStoreKeys {
        val currencyKey = stringPreferencesKey(name = Constants.DATA_STORE_KEY_CURRENCY)
        val sortByKey = stringPreferencesKey(name = Constants.DATA_STORE_KEY_SORT_BY)
        val sortOrderKey = stringPreferencesKey(name = Constants.DATA_STORE_KEY_SORT_ORDER)
        val themeKey = stringPreferencesKey(name = Constants.DATA_STORE_KEY_THEME)
    }

    /**
     * Updates the currency setting.
     */
    suspend fun updateCurrency(currency: Currency) = dataStore.edit { preference ->
        preference[DataStoreKeys.currencyKey] = currency.code
    }

    /**
     * Updates the sort order setting.
     */
    suspend fun updateSortOrder(sortOrder: SortOrder) = dataStore.edit { preference ->
        preference[DataStoreKeys.sortOrderKey] = sortOrder::class.java.name
        preference[DataStoreKeys.sortByKey] = sortOrder.sortBy.name
    }

    /**
     * Updates the theme setting.
     */
    suspend fun updateTheme(themeMode: ThemeMode) = dataStore.edit { preference ->
        preference[DataStoreKeys.themeKey] = themeMode.name
    }

    /**
     * Retrieves the current currency setting as a Flow.
     */
    val getCurrency: Flow<Currency> = dataStore.data.map { preference ->
        Currency.valueOf(code = preference[DataStoreKeys.currencyKey] ?: Currency.PKR.code)
    }

    /**
     * Retrieves the current sort order setting as a Flow.
     */
    val getSortOrder: Flow<SortOrder> = dataStore.data.map { preference ->
        val mSortBy = SortBy.valueOf(
            value = preference[DataStoreKeys.sortByKey] ?: SortBy.Descending.name
        )

        when (preference[DataStoreKeys.sortOrderKey]) {
            SortOrder.Amount::class.java.name -> SortOrder.Amount(sortBy = mSortBy)
            SortOrder.Name::class.java.name -> SortOrder.Name(sortBy = mSortBy)
            else -> SortOrder.Date(sortBy = mSortBy)
        }
    }

    /**
     * Retrieves the current theme setting as a Flow.
     */
    val getTheme: Flow<ThemeMode> = dataStore.data.map { preference ->
        runCatching {
            ThemeMode.valueOf(value = preference[DataStoreKeys.themeKey] ?: ThemeMode.System.name)
        }.getOrDefault(defaultValue = ThemeMode.System)
    }
}