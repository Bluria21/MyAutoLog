package com.blruia.mycar.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.blruia.mycar.R
import com.blruia.mycar.data.CarInfo

@Composable
fun ChangeCarDialog(
    onDismiss: () -> Unit,
    onSave: (CarInfo) -> Unit
) {
    val carName = remember { mutableStateOf("") }
    val carBrand = remember { mutableStateOf("") }
    val carYear = remember { mutableStateOf("") }
    val carMileage = remember { mutableStateOf("") }
    val carImageUri = remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { carImageUri.value = it }
    }

    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colors.surface,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Заголовок
            Text(
                text = stringResource(R.string.car),
                color = colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(stringResource(R.string.car_image))
                }
                carImageUri.value?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    )
                }
                OutlinedTextField(
                    value = carName.value,
                    onValueChange = { carName.value = it },
                    label = { Text(stringResource(R.string.pole_name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = carBrand.value,
                    onValueChange = { carBrand.value = it },
                    label = { Text(stringResource(R.string.make_model)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = carMileage.value,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            carMileage.value = it
                        }
                    },
                    label = { Text(stringResource(R.string.label_mileage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                DateCarouselChangeCarView(
                    selectedYear = carYear.value,
                    onYearSelected = { carYear.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.errorContainer
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        stringResource(R.string.action_close),
                        color = colors.onErrorContainer
                    )
                }
                Button(
                    onClick = {
                        onSave(
                            CarInfo(
                                nickname = carName.value,
                                brand = carBrand.value,
                                yearCar = carYear.value,
                                mileageCar = carMileage.value,
                                imageUri = carImageUri.value?.toString()
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary
                    )
                ) {
                    Text(
                        stringResource(R.string.action_save),
                        color = colors.onPrimary
                    )
                }
            }
        }
    }
}