package com.blruia.mycar.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Entry::class,CarInfo::class,CustomCard::class],
    version = 4,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun entryDao():EntryDao
    abstract fun customCardDao():CustomCardDao
    }

