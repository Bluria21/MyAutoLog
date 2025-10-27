package com.blruia.mycar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.blruia.mycar.R
import com.blruia.mycar.ui.theme.ThemeManager
import kotlinx.coroutines.launch

@Composable
fun TheApplicationView(
    modifier: Modifier = Modifier,
    navController: NavController,
    themeManager: ThemeManager
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background) // Фон всего экрана черный
    )  {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Меню",
                tint = colors.onSurface,  // <-- всегда контрастный к surface
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
                text = stringResource(R.string.menu_about),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 50.dp),
                color = colors.onSurface,   // <-- всегда контрастный к surface
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

        }
    }
}