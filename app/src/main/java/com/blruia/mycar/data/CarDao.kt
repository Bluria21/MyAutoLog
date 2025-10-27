package com.blruia.mycar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    //Данные полученные "Название детали, пробег и дата"
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarInfo):Long

    @Query("SELECT * FROM carinfo")
    fun getAllCars(): Flow<List<CarInfo>>

    @Delete
    suspend fun deleteCar(car: CarInfo)

    @Query("DELETE FROM carinfo")
    suspend fun clearAll()
}
