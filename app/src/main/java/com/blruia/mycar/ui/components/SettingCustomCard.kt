package com.blruia.mycar.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.blruia.mycar.R
import com.blruia.mycar.data.CustomCard
import com.blruia.mycar.domain.CustomCardViewModel

@Composable
fun SettingCustomCard(
    carId: Int,
    customCardViewModel: CustomCardViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var interval by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_new_detail)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.name_detail)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = interval,
                    onValueChange = { interval = it },
                    label = { Text(stringResource(R.string.intervals_replacement)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) {
                    customCardViewModel.addPart(
                        CustomCard(
                            carId = carId,
                            name = name,
                            intervals = interval.toIntOrNull()
                        )
                    )
                    onDismiss()
                }
            }) {
                Text(stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.action_close))
            }
        }
    )
}


