package com.blruia.mycar.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blruia.mycar.data.Entry
import com.blruia.mycar.data.EntryDao
import com.blruia.mycar.data.FirestoreRepository
import com.blruia.mycar.di.databasemodule.DataStoreManager
import com.blruia.mycar.ui.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val entryDao: EntryDao,
    val dataStoreManager: DataStoreManager,
    val themeManager: ThemeManager,
    val firestoreRepo: FirestoreRepository
) : ViewModel() {

    private val _replacementIntervals = MutableStateFlow<Map<String, Int>>(emptyMap())
    val replacementIntervals: StateFlow<Map<String, Int>> = _replacementIntervals

    private val _activeCarId = MutableStateFlow<Int?>(null)


    val entries: StateFlow<List<Entry>> = _activeCarId
        .filter { it != null && it != -1 }  // ✅ здесь уже оба условия
        .flatMapLatest { carId -> entryDao.getEntriesForCar(carId!!) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        observeActiveCar()
        loadIntervals()
    }

    private fun observeActiveCar() {
        viewModelScope.launch {
            dataStoreManager.getActiveCarId().collect { id ->
                _activeCarId.value = id
            }
        }
    }

    private fun loadIntervals() {
        viewModelScope.launch {
            dataStoreManager.loadIntervals().collect { intervals ->
                _replacementIntervals.value = intervals
            }
        }
    }

    fun saveInterval(partName: String, value: Int) {
        viewModelScope.launch {
            dataStoreManager.saveInterval(partName, value)
            loadIntervals() // Обновляем данные после сохранения
        }
    }

    fun addEntry(entry: Entry) {
        viewModelScope.launch(Dispatchers.IO) {
            _activeCarId.value?.let { carId ->
                val newId = entryDao.insertEntry(entry.copy(carId = carId)).toInt()
                firestoreRepo.uploadEntry(entry.copy(id = newId, carId = carId))
            }
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch(Dispatchers.IO) {
            entryDao.deleteEntry(entry)
            firestoreRepo.deleteEntry(entry.carId, entry.id)
        }
    }
}