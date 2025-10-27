package com.blruia.mycar.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blruia.mycar.di.databasemodule.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun getLanguage(): Flow<String> = dataStoreManager.getLanguage()

    fun saveLanguage(lang: String) {
        viewModelScope.launch {
            dataStoreManager.saveLanguage(lang)
        }
    }
}