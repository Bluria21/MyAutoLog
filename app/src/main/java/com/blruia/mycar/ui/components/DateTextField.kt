package com.blruia.mycar.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blruia.mycar.R
import java.util.Calendar

//Дата для карточки на главной странице
@Composable
fun DateTextField(dateState: MutableState<String>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val colors = MaterialTheme.colorScheme

    // Открываем DatePickerDialog
    val openDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = "%02d.%02d.%d".format(dayOfMonth, month + 1, year)
                dateState.value = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Button(
        onClick = { openDatePicker() },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colors.background),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),


    ) {
        Text(
            modifier = Modifier.background(colors.background),
            text = if (dateState.value.isEmpty()) (stringResource(R.string.label_select_date)) else dateState.value,
            color = colors.onSurface,


        )
    }

}