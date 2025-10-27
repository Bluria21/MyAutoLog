package com.blruia.mycar.di.databasemodule

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.blruia.mycar.ui.theme.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val SETTINGS_DATASTORE = "settings"

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(SETTINGS_DATASTORE)
        }
    }


    @Provides
    @Singleton
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return DataStoreManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideThemeManager(dataStore: DataStore<Preferences>): ThemeManager {
        return ThemeManager(dataStore)
    }
}