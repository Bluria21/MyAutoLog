package com.blruia.mycar.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "entries",
    foreignKeys = [
        ForeignKey(
            entity = CarInfo::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("carId")]
)
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carId: Int,
    val partKey: String,
    val date: String,
    val mileageHistory: String,
    val price: String,
    val isResourceKey: Boolean = true
)

@Entity(tableName = "carInfo")
data class CarInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nickname: String,
    val brand: String,
    val mileageCar: String,
    val yearCar: String,
    val imageUri: String? = null,
)

@Entity(tableName = "parts")
data class CustomCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val carId: Int,
    val name:String,
    val intervals:Int?
)








