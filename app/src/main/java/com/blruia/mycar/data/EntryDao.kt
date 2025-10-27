package com.blruia.mycar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: Entry):Long

    @Query("SELECT * FROM entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<Entry>>

    @Delete
    suspend fun deleteEntry(entry: Entry)

    @Query("SELECT * FROM entries WHERE carId = :carId ORDER BY date DESC")
    fun getEntriesForCar(carId: Int): Flow<List<Entry>>

    @Query("DELETE FROM entries WHERE carId = :carId")
    suspend fun clearEntriesForCar(carId: Int)
}
