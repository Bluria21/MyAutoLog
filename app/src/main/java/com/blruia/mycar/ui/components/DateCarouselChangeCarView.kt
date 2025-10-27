package com.blruia.mycar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blruia.mycar.R
import java.util.Calendar

//Карусель даты
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateCarouselChangeCarView(
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val years = remember {
        (1950..Calendar.getInstance().get(Calendar.YEAR)).toList().reversed()
    }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedYear,
            onValueChange = {},
            label = { Text((stringResource(R.string.year_manufacture))) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 300.dp)
        ) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(text = year.toString()) },
                    onClick = {
                        onYearSelected(year.toString())
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}