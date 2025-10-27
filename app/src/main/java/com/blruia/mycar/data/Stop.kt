package com.blruia.mycar.data

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.blruia.mycar.MainActivity
import com.blruia.mycar.R
import com.blruia.mycar.di.databasemodule.DataStoreManager
import com.blruia.mycar.domain.CarInfoViewModel
import com.blruia.mycar.ui.screens.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class Stop : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private lateinit var auth: FirebaseAuth
    private val carInfoViewModel: CarInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val justSignedIn = intent.getBooleanExtra("JUST_SIGNED_IN", false)


        auth = FirebaseAuth.getInstance()

        setContent { SplashScreen() }

        lifecycleScope.launch {
            // читаем язык асинхронно
            val language = dataStoreManager.getLanguage().first()
            updateLocale(language) // выставляем локаль
            delay(800) // для красоты анимации Splash

            val currentUser = auth.currentUser
            if(currentUser != null) {
                if (justSignedIn) {
                    // вызываем миграцию локальных данных в облако
                    carInfoViewModel.migrateLocalToCloudAndSync()
                } else {
                    // обычная синхронизация с облаком при запуске
                    carInfoViewModel.syncFromCloud()
                }
            }
            checkAuthAndNavigate()

        }
    }

    private fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun checkAuthAndNavigate() {
        val currentUser = auth.currentUser
        val nextActivity = if (currentUser != null) {
            MainActivity::class.java   // уже вошёл
        } else {
            LoginActivity::class.java  // ещё не вошёл
        }
        startActivity(Intent(this, nextActivity))
        finish()
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.img),
        contentDescription = "Splash Image",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}