package com.blruia.mycar.di.databasemodule

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val INTERVAL_KEYS = listOf(
            "part_timing_belt",
            "part_motor_oil",
            "part_spark_plugs",
            "part_pads",
            "part_brake_fluid",
            "part_tires"
        )

        private val ACTIVE_CAR_ID = intPreferencesKey("active_car_id")
        private val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    suspend fun saveLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }
    fun getLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }
    }

    private fun getKey(partName: String): Preferences.Key<Int> {
        return intPreferencesKey(partName)
    }

    suspend fun saveInterval(partName: String, value: Int) {
        val key = getKey(partName)
        dataStore.edit { settings ->
            settings[key] = value
        }
    }

    fun loadIntervals(): Flow<Map<String, Int>> {
        return dataStore.data.map { prefs ->
            INTERVAL_KEYS.associateWith { partName ->
                prefs[getKey(partName)] ?: getDefaultIntervals()[partName]!!
            }
        }
    }

    suspend fun saveActiveCarId(carId: Int) {
        dataStore.edit { prefs ->
            prefs[ACTIVE_CAR_ID] = carId
        }
    }

    fun getActiveCarId(): Flow<Int?> {
        return dataStore.data.map { prefs ->
            prefs[ACTIVE_CAR_ID]
        }
    }

    private fun getDefaultIntervals() = mapOf(
        "part_timing_belt" to 90_000,
        "part_motor_oil" to 15_000,
        "part_spark_plugs" to 30_000,
        "part_pads" to 30_000,
        "part_brake_fluid" to 50_000,
        "part_tires" to 100_000
    )


}