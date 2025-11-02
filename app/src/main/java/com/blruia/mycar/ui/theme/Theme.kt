package com.blruia.mycar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    background = Color.Black,
    surface = Color(0xFF1E1E1E),
    onBackground = Color.DarkGray,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onBackground = Color.LightGray,
)
@Composable
fun MyCarTheme(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}