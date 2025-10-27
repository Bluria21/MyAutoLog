package com.blruia.mycar.ui.components

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//Кастмный переключатель темы
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun CustomButtonTheme(
    isDark: Boolean,
    onToggle: () -> Unit
) {
    val togglePosition by animateDpAsState(
        targetValue = if (isDark) 40.dp else 0.dp,
        label = "toggle_anim"
    )

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.DarkGray)
            .clickable { onToggle() }
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Светлая тема",
                tint = Color.Yellow,
                modifier = Modifier.size(24.dp)
            )
            Icon(
                imageVector = Icons.Default.NightsStay,
                contentDescription = "Темная тема",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
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
