package com.blruia.mycar

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.blruia.mycar.di.databasemodule.DataStoreManager
import com.blruia.mycar.ui.screens.MainScreen
import com.blruia.mycar.ui.theme.MyCarTheme
import com.blruia.mycar.ui.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )

        setContent {
            val isDarkTheme by themeManager.isDarkMode.collectAsState(initial = false)

            MyCarTheme(isDarkTheme) {
                MainScreen(
                    themeManager = themeManager,
                    dataStoreManager = dataStoreManager
                )
            }
        }
    }
}







