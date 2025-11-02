package com.blruia.mycar.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blruia.mycar.domain.SettingsViewModel
import com.blruia.mycar.uti.setAppLocale
import kotlinx.coroutines.launch

//Кастмный переключатель языка
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun CustomButtonLanguage(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val currentLanguage by settingsViewModel.getLanguage().collectAsState(initial = "en")

    var isEnglish by remember(currentLanguage) {
        mutableStateOf(currentLanguage == "en")
    }

    val togglePosition by animateDpAsState(
        targetValue = if (isEnglish) 0.dp else 40.dp,
        label = "language_toggle"
    )

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.DarkGray)
            .clickable {
                coroutineScope.launch {
                    val newLang = if (isEnglish) "ru" else "en"
                    settingsViewModel.saveLanguage(newLang)
                    setAppLocale(context, newLang)
                isEnglish = !isEnglish
                    (context as? Activity)?.recreate()
                }
            }
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("EN", color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(start = 6.dp))
            Text("RU", color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(end = 6.dp))
        }

        Box(
            modifier = Modifier
                .offset(x = togglePosition)
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}