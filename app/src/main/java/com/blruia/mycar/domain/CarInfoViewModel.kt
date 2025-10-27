package com.blruia.mycar.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blruia.mycar.data.CarDao
import com.blruia.mycar.data.CarInfo
import com.blruia.mycar.data.EntryDao
import com.blruia.mycar.data.FirestoreRepository
import com.blruia.mycar.di.databasemodule.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class CarInfoViewModel @Inject constructor(
    private val carDao: CarDao,
    private val entryDao: EntryDao,
    val firestoreRepo: FirestoreRepository,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    val cars: StateFlow<List<CarInfo>> = carDao.getAllCars().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Устанавливаем активный автомобиль
    fun setActiveCar(carId: Int) {
        viewModelScope.launch {
            dataStoreManager.saveActiveCarId(carId)
        }
    }
    fun migrateLocalToCloudAndSync() {
        // создаём независимую корутину, не зависящую от жизненного цикла ViewModel
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            try {
                val localCars = carDao.getAllCars().first()

                for (localCar in localCars) {
                    firestoreRepo.uploadCar(localCar)

                    val entries = entryDao.getEntriesForCar(localCar.id).firstOrNull() ?: emptyList()
                    for (entry in entries) {
                        firestoreRepo.uploadEntry(entry)
                    }
                }

                syncFromCloud()

                Log.d("CarInfoViewModel", "✅ migrateLocalToCloudAndSync успешно завершена")

            } catch (e: CancellationException) {
                Log.w("CarInfoViewModel", "⚠️ migrateLocalToCloudAndSync отменена", e)
            } catch (e: Exception) {
                Log.e("CarInfoViewModel", "❌ migrateLocalToCloudAndSync failed: ${e.message}", e)
            }
        }
    }

    // Получаем активный автомобиль
    fun getActiveCar(): Flow<CarInfo?> {
        return cars.combine(dataStoreManager.getActiveCarId()) { carsList, activeId ->
            activeId?.let { id -> carsList.find { it.id == id } }
        }
    }

    // При добавлении автомобиля автоматически делаем его активным
    fun addCar(car: CarInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            val newId = carDao.insertCar(car).toInt()
            val carWithId = car.copy(id = newId)
            setActiveCar(newId)
            firestoreRepo.uploadCar(carWithId)
        }
    }

    // При удалении автомобиля сбрасываем активный, если удаляем текущий
    fun deleteCar(car: CarInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            carDao.deleteCar(car)
            firestoreRepo.deleteCar(car.id)
            dataStoreManager.getActiveCarId().first()?.let { activeId ->
                if (activeId == car.id) {
                    dataStoreManager.saveActiveCarId(-1)
                }
            }
        }
    }
    suspend fun syncFromCloud() = withContext(Dispatchers.IO) {
        val cars = firestoreRepo.downloadCarsAsync()

        carDao.clearAll()

        for (car in cars) {
            val newCarId = carDao.insertCar(car).toInt()

            val entries = firestoreRepo.downloadEntriesAsync(car.id)

            entryDao.clearEntriesForCar(newCarId)
            entries.forEach { entryDao.insertEntry(it.copy(carId = newCarId)) }
        }
    }
}

