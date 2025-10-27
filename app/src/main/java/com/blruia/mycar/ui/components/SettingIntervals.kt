package com.blruia.mycar.ui.components

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blruia.mycar.R
import com.blruia.mycar.domain.EntryViewModel
import com.blruia.mycar.uti.getLocalizedPartName
import kotlinx.coroutines.launch

@Composable
fun SettingIntervals(
    viewModel: EntryViewModel
){
    val colors = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as Activity
    var isIntervalsExpanded by remember { mutableStateOf(false) }
    val replacementIntervals by viewModel.replacementIntervals.collectAsState()

    Card(
        colors = CardDefaults.cardColors(containerColor = colors.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isIntervalsExpanded = !isIntervalsExpanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.intervals),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )
            AnimatedVisibility(visible = isIntervalsExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.onBackground, RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 425.dp)
                    ) {
                        items(replacementIntervals.keys.toList()) { partName ->
                            var textState by remember {
                                mutableStateOf(replacementIntervals[partName]?.toString() ?: "")
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(getLocalizedPartName(partName)),
                                    color = colors.onSurface,
                                    fontSize = 18.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = textState,
                                    onValueChange = { newText ->
                                        textState = newText
                                        newText.toIntOrNull()?.let { newValue ->
                                            coroutineScope.launch {
                                                viewModel.saveInterval(partName, newValue)
                                            }
                                        }
                                    },
                                    modifier = Modifier.width(100.dp),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    singleLine = true,
                                    textStyle = TextStyle(color = colors.onSurface)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
