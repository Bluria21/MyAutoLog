package com.blruia.mycar.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.blruia.mycar.R
import com.blruia.mycar.data.CarInfo
import com.blruia.mycar.di.databasemodule.DataStoreManager
import com.blruia.mycar.domain.CarInfoViewModel
import com.blruia.mycar.ui.components.ChangeCarDialog
import com.blruia.mycar.ui.theme.ThemeManager
import kotlinx.coroutines.launch

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navController: NavController,
    themeManager: ThemeManager,
    viewModel: CarInfoViewModel,
    dataStoreManager: DataStoreManager
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val isDarkTheme by themeManager.isDarkMode.collectAsState(initial = false)
    var showCarDialog by remember { mutableStateOf(false) }

    val cars by viewModel.cars.collectAsState()
    val activeCarId by dataStoreManager.getActiveCarId().collectAsState(initial = null)

    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color.Black else Color.White
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Назад",
                    tint = colors.onSurface,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 10.dp)
                        .clickable {
                            scope.launch {
                                drawerState.close()
                                navController.navigate("mainScreen")
                            }
                        }
                )
                Text(
                    text = stringResource(R.string.profile),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 50.dp),
                    color = colors.onSurface,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (cars.isEmpty()) {
                    Text(
                        text = stringResource(R.string.add_car),
                        color = colors.onSurface,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    cars.forEach { car ->
                        val isSelected = car.id == activeCarId
                        CarListItem(
                            car = car,
                            isSelected = isSelected,
                            onClick = {
                                scope.launch {
                                    viewModel.setActiveCar(car.id)
                                }
                            },
                            onDelete = { viewModel.deleteCar(car) },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Добавить авто",
                    tint = colors.primary,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable { showCarDialog = true }
                )
            }


        }

        if (showCarDialog) {
            Dialog(
                onDismissRequest = { showCarDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                ChangeCarDialog(
                    onDismiss = { showCarDialog = false },
                    onSave = { newCar ->
                        viewModel.addCar(newCar)
                        showCarDialog = false
                    }
                )
            }
        }
    }
}
@Composable
fun CarListItem(
    car: CarInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.onBackground)
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        car.imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Изображение автомобиля",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } ?: Icon(
            Icons.Default.Commute,
            contentDescription = "Автомобиль",
            modifier = Modifier.size(50.dp),
            tint = colors.onSurface
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(text = car.nickname, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            Text(text = car.brand, style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
            Text(text = "${stringResource(R.string.year)}: ${car.yearCar}", style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
            Text(text = "${stringResource(R.string.label_mileage)}: ${car.mileageCar} км", style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
        }

        IconButton(onClick = { showDialog = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = colors.error)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(stringResource(R.string.confirmation))
            },
            text = {
                Text(stringResource(R.string.are_you_sure))
            },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDialog = false
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}







