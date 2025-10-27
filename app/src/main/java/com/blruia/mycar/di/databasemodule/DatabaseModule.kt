package com.blruia.mycar.di.databasemodule

import android.content.Context
import androidx.room.Room
import com.blruia.mycar.data.AppDatabase
import com.blruia.mycar.data.CarDao
import com.blruia.mycar.data.CustomCardDao
import com.blruia.mycar.data.EntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideEntryDao(database: AppDatabase):EntryDao{
        return database.entryDao()
    }

    @Provides
    @Singleton
    fun provideCarDao(database: AppDatabase): CarDao {
        return database.carDao()
    }
    @Provides
    @Singleton
    fun provideCustomCardDao(database: AppDatabase): CustomCardDao {
        return database.customCardDao()
    }
}
