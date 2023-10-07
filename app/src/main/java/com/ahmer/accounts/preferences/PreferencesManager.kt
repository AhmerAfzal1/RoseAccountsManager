package com.ahmer.accounts.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.ahmer.accounts.utils.Constants
import com.ahmer.accounts.utils.SortBy
import com.ahmer.accounts.utils.ThemeMode
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCES_NAME)

    fun getTheme(): Flow<ThemeMode> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            Log.e(Constants.LOG_TAG, "PreferenceException: ${e.localizedMessage}", e)
            FirebaseCrashlytics.getInstance().recordException(e)
            emit(emptyPreferences())
        } else throw e
    }.map { preference ->
        ThemeMode.valueOf(value = preference[PreferencesKeys.APP_THEME] ?: ThemeMode.System.name)
    }.distinctUntilChanged()

    fun getSortOrder(): Flow<SortBy> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            Log.e(Constants.LOG_TAG, "PreferenceException: ${e.localizedMessage}", e)
            FirebaseCrashlytics.getInstance().recordException(e)
            emit(emptyPreferences())
        } else throw e
    }.map { preference ->
        SortBy.valueOf(value = preference[PreferencesKeys.SORT_ORDER] ?: SortBy.DATE.name)
    }.distinctUntilChanged()

    suspend fun updateTheme(themeMode: ThemeMode) = context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.APP_THEME] = themeMode.name
    }

    suspend fun updateSortOrder(sortBy: SortBy) = context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.SORT_ORDER] = sortBy.name
    }

}