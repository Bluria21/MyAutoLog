package com.blruia.mycar.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blruia.mycar.R
import com.blruia.mycar.data.Entry
import com.blruia.mycar.domain.EntryViewModel
import com.blruia.mycar.ui.theme.ThemeManager
import com.blruia.mycar.uti.getLocalizedPartName

@Composable
fun CardView(
    title: String,
    viewModel: EntryViewModel,
    themeManager: ThemeManager,
    carId: Int,
    isResourceKey: Boolean = true,
    customInterval: Int? = null ,
    onDelete: ((String) -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    val entries by viewModel.entries.collectAsState(initial = emptyList())
    val dateState = rememberSaveable { mutableStateOf("") }
    val mileageState = rememberSaveable { mutableStateOf("") }
    val priceState = rememberSaveable { mutableStateOf("") }

    val colors = MaterialTheme.colorScheme
    val replacementIntervals by viewModel.replacementIntervals.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }


    val mileageToAdd = customInterval ?: (replacementIntervals[title] ?: 50_000)

    val lastMileage = entries
        .filter { it.partKey == title }
        .maxByOrNull { it.mileageHistory.toIntOrNull() ?: 0 }
        ?.mileageHistory?.toIntOrNull() ?: 0
    val nextReplacementMileage = lastMileage + mileageToAdd

    val displayTitle = if (isResourceKey) {
        stringResource(getLocalizedPartName(title))
    } else {
        title
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = colors.onBackground),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { isExpanded = !isExpanded }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Text(
                text = displayTitle,
                color = colors.onSurface,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            AnimatedVisibility(visible = isExpanded) {
                Box(
                    modifier = Modifier
                        .background(colors.onBackground, shape = RoundedCornerShape(12.dp))
                        .padding(7.dp)
                ) {
                    Column(modifier = Modifier.background(colors.onBackground)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().background(colors.onBackground),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.label_next_replacement),
                                color = colors.onSurface,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(10.dp)
                            )
                            Text(
                                text = nextReplacementMileage.toString(),
                                color = colors.onSurface,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        DateTextField(dateState)
                        Spacer(modifier = Modifier.height(18.dp))
                        Mileage(mileageState)
                        Spacer(modifier = Modifier.height(25.dp))
                        Description(priceState)
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isResourceKey && onDelete != null) {
                                IconButton(onClick = { showDeleteDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Удалить",
                                        tint = Color.Red
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                onClick = {
                                    if (dateState.value.isNotBlank() &&
                                        mileageState.value.isNotBlank() &&
                                        priceState.value.isNotBlank()
                                    ) {
                                        val newEntry = Entry(
                                            date = dateState.value,
                                            mileageHistory = mileageState.value,
                                            price = priceState.value,
                                            partKey = title,
                                            carId = carId,
                                            isResourceKey = isResourceKey
                                        )
                                        viewModel.addEntry(newEntry)
                                        dateState.value = ""
                                        mileageState.value = ""
                                        priceState.value = ""
                                        isExpanded = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text(
                                    text = stringResource(R.string.action_add),
                                    color = colors.onSurface
                                )
                            }
                        }
                    }
                }

            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    onDelete?.invoke(title)
                    showDeleteDialog = false
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            },
            title = { Text("Удаление") },
            text = { Text("Вы уверены, что хотите удалить «$displayTitle»?") }
        )
    }
}


