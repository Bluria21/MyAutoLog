package com.blruia.mycar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomCardDao {
    @Query("SELECT * FROM parts WHERE carId = :carId")
    fun getPartsForCar(carId: Int): Flow<List<CustomCard>>

    @Insert
    suspend fun insertPart(part: CustomCard)

    @Delete
    suspend fun deletePart(part: CustomCard)

}