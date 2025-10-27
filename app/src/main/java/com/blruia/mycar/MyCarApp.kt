package com.blruia.mycar

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyCarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
  //  override fun onCreate() {
  //      super.onCreate()
//
  //      // Получение языка и установка локали
    //    val language = runBlocking {
      //      EntryPointAccessors.fromApplication(
        //        this@MyCarApp,
          //      DataStoreEntryPoint::class.java
            //).dataStoreManager.getLanguage().first()
        //}
//
  //      setAppLocale(language)
    //}
//
  //  private fun setAppLocale(language: String) {
    //    val locale = Locale(language)
      //  Locale.setDefault(locale)
        //val config = Configuration()
        //config.setLocale(locale)
       // baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
   // }
