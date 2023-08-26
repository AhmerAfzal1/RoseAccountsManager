package com.rose.account.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rose.account.utils.Constants
import com.rose.account.utils.SortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    private object PreferencesKeys {
        val APP_LOCK = booleanPreferencesKey("AppLockKey")
        val APP_THEME = booleanPreferencesKey("AppThemeKey")
        val SORT_ORDER = stringPreferencesKey("SortOrderKey")
    }

    data class FilterPreferences(
        val appLock: Boolean,
        val appTheme: Boolean,
        val sortOrder: SortOrder
    )

    val preferencesFlow = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(Constants.LOG_TAG, "PreferencesException: ${exception.message}", exception)
                FirebaseCrashlytics.getInstance().recordException(exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val lockIsChecked = preferences[PreferencesKeys.APP_LOCK] ?: false
            val themeIsChecked = preferences[PreferencesKeys.APP_THEME] ?: false
            val sortOrder =
                SortOrder.valueOf(preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name)
            FilterPreferences(lockIsChecked, themeIsChecked, sortOrder)
        }

    suspend fun updateAppLock(isChecked: Boolean) = context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.APP_LOCK] = isChecked
    }

    suspend fun updateAppTheme(isChecked: Boolean) = context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.APP_THEME] = isChecked
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) = context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
    }

}