package com.blruia.mycar.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import com.blruia.mycar.R
import com.blruia.mycar.domain.EntryViewModel
import com.blruia.mycar.ui.theme.ThemeManager
import com.blruia.mycar.uti.getLocalizedPartName
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryView(
    navController: NavController,
    themeManager: ThemeManager,
    viewModel: EntryViewModel
) {
    val isDarkTheme by themeManager.isDarkMode.collectAsState(initial = false)
    val colors = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    val entries by viewModel.entries.collectAsState(initial = emptyList())
    val sortedEntries = entries.sortedByDescending { it.date }
    var showDelete by remember { mutableStateOf(false) }

    Column(modifier = Modifier.background(colors.background)) {
        // Верхняя панель
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.ChevronLeft,
                contentDescription = "Назад",
                tint = colors.onSurface,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        scope.launch { navController.navigate("mainScreen") }
                    }
            )
            Text(
                text = stringResource(R.string.menu_story),
                modifier = Modifier.weight(1f).padding(end = 50.dp),
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = colors.onSurface)
            )
        }

        // Заголовки таблицы
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = stringResource(R.string.label_date), modifier = Modifier.weight(2f), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.onSurface), textAlign = TextAlign.Center)
            Text(text = stringResource(R.string.label_mileage), modifier = Modifier.weight(1.2f), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.onSurface), textAlign = TextAlign.Center)
            Text(text = stringResource(R.string.label_price), modifier = Modifier.weight(1.2f), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.onSurface), textAlign = TextAlign.Center)
            Text(text = stringResource(R.string.title_part), modifier = Modifier.weight(1.6f), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.onSurface), textAlign = TextAlign.Center)
        }

        // Список записей
        LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 5.dp)) {
            items(sortedEntries) { item ->
                // Для каждой записи решаем: ресурсный ключ -> локализованный текст, иначе просто partKey
                val partTitle = if(item.isResourceKey) {
                    getLocalizedPartName(item.partKey)?.let { resId ->
                        stringResource(resId)
                    } ?: item.partKey
                } else {
                    item.partKey // просто название кастомной детали
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                        .combinedClickable(
                            onClick = { showDelete = false },
                            onLongClick = { showDelete = true }
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.date, modifier = Modifier.weight(2f), color = colors.onSurface, textAlign = TextAlign.Center)
                    Text(text = item.mileageHistory, modifier = Modifier.weight(1.2f), color = colors.onSurface, textAlign = TextAlign.Center)
                    Text(text = item.price, modifier = Modifier.weight(1.2f), color = colors.onSurface, textAlign = TextAlign.Center)
                    Text(text = partTitle, modifier = Modifier.weight(1.6f), color = colors.onSurface, textAlign = TextAlign.Center)

                    AnimatedVisibility(visible = showDelete) {
                        IconButton(onClick = {
                            viewModel.deleteEntry(item)
                            showDelete = false
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}


