package com.blruia.mycar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.blruia.mycar.R

@Composable
fun Description(descriptionState: MutableState<String>){
    val colors = MaterialTheme.colorScheme
    TextField(
        value = descriptionState.value,
        onValueChange = { descriptionState.value = it },
        label = { Text((stringResource(R.string.label_price)), color = colors.onSurface) },
        textStyle = TextStyle(color = colors.onSurface),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colors.background,
            focusedContainerColor = colors.background,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    )
}