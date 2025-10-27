package com.blruia.mycar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.blruia.mycar.data.CarInfo

@Composable
fun ScrollCarView(
    cars: List<CarInfo>,
    activeCarId: Int?,
    navController: NavController,
    onCarClick: (CarInfo) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        items(cars) { car ->
            val isActive = car.id == activeCarId
            val borderColor = if (isActive) Color.Green else Color.Transparent
            val backgroundColor = if (isActive) Color.Green.copy(alpha = 0.1f) else Color.Transparent

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                    .clickable { onCarClick(car) }
                    .padding(8.dp), // отступ внутри рамки
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Иконка автомобиля
                car.imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                } ?: Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    tint = colors.onSurface
                )

                // Информация об авто
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = car.nickname,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Text(
                        text = "${car.brand}, ${car.yearCar} • ${car.mileageCar} км",
                        fontSize = 12.sp,
                        color = colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}