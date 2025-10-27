package com.blruia.mycar.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blruia.mycar.data.CustomCard
import com.blruia.mycar.data.CustomCardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomCardViewModel @Inject constructor(
    private val customCardDao: CustomCardDao
) : ViewModel() {

    fun getPartsForCar(carId: Int): Flow<List<CustomCard>> {
        return customCardDao.getPartsForCar(carId)
    }

    fun addPart(part: CustomCard) {
        viewModelScope.launch {
            customCardDao.insertPart(part)
        }
    }

    fun deletePart(part: CustomCard) {
        viewModelScope.launch {
            customCardDao.deletePart(part)
        }
    }
}
