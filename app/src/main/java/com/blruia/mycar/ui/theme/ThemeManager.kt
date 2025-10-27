package com.blruia.mycar.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

class ThemeManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    val isDarkMode: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences -> preferences[DARK_MODE_KEY] ?: false }

    suspend fun toggleTheme() {
        dataStore.edit { preferences ->
            val currentMode = preferences[DARK_MODE_KEY] ?: false
            preferences[DARK_MODE_KEY] = !currentMode
        }
    }

    fun getCurrentTheme(): Boolean {
        return runBlocking {
            dataStore.data.first()[DARK_MODE_KEY] ?: false
        }
    }
}