package com.blruia.mycar.di.databasemodule

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DataStoreEntryPoint {
    val dataStoreManager: DataStoreManager
}