package com.blruia.mycar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.blruia.mycar.R
import com.blruia.mycar.di.databasemodule.DataStoreManager
import com.blruia.mycar.domain.CarInfoViewModel
import com.blruia.mycar.domain.CustomCardViewModel
import com.blruia.mycar.domain.EntryViewModel
import com.blruia.mycar.ui.components.CardView
import com.blruia.mycar.ui.theme.ThemeManager

@Composable
fun MainScreen(
    themeManager: ThemeManager,
    dataStoreManager: DataStoreManager
) {
    AppNavigation(
        themeManager = themeManager,
        dataStoreManager = dataStoreManager
    )
}
@Composable
fun AppNavigation(
    themeManager: ThemeManager,
    dataStoreManager: DataStoreManager,
) {
    val navController = rememberNavController()

    val entryViewModel: EntryViewModel = hiltViewModel()
    val carInfoViewModel: CarInfoViewModel = hiltViewModel()
    val customCardViewModel: CustomCardViewModel = hiltViewModel()


    val carIdFlow = remember { dataStoreManager.getActiveCarId() }
    val carId by carIdFlow.collectAsState(initial = null)

    val startDestination = if (carId != null) "mainScreen" else "profile"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("mainScreen") {
            MenuView(
                navController = navController,
                themeManager = themeManager,
                viewModel = carInfoViewModel,
                carId = carId,
                dataStoreManager = dataStoreManager // ✅ добавлено
            )

        }
        composable("infoView") {
            Avtor(
                navController = navController,
                themeManager = themeManager,
                viewModel = entryViewModel,
                customCardViewModel = customCardViewModel,
                carId = carId ?: 0
                )
        }
        composable("settingView") {
            HistoryView(navController, themeManager, entryViewModel)
        }
        composable("supportView") {
            SupportView(Modifier, navController, themeManager)
        }
        composable("theapplicationView") {
            TheApplicationView(Modifier, navController, themeManager)
        }
        composable("profile") {
            Profile(Modifier,navController, themeManager, carInfoViewModel, dataStoreManager)
        }
        composable("statistics") {
            Statistics(Modifier, navController, themeManager)
        }
    }
}

@Composable
fun IconView(
    modifier: Modifier = Modifier,
    navController: NavController,
    themeManager: ThemeManager,
    viewModel: EntryViewModel = hiltViewModel(),
    dataStoreManager: DataStoreManager,
    carId: Int,
) {
    val customCardViewModel: CustomCardViewModel = hiltViewModel()
    val customParts by customCardViewModel.getPartsForCar(carId).collectAsState(initial = emptyList())
    val colors = MaterialTheme.colorScheme
    val items = listOf(
        "part_timing_belt",
        "part_motor_oil",
        "part_spark_plugs",
        "part_pads",
        "part_brake_fluid",
        "part_tires"
    )
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        item {
            Text(
                text = (stringResource(R.string.title_parts)),
                modifier = Modifier
                    .padding(top = 25.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    color = colors.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
        items(items) { partKey ->
            CardView(
                title = partKey,
                viewModel = viewModel,
                themeManager = themeManager,
                carId = carId,
                isResourceKey = true,   // можно опустить — по умолчанию true
                customInterval = null
            )
        }

        items(customParts) { part ->
            // Это внутри @Composable, так что все ок
            CardView(
                title = part.name,
                viewModel = viewModel,
                themeManager = themeManager,
                carId = carId ,
                isResourceKey = false,         // важно: не пытаться локализовать
                customInterval = part.intervals,
                onDelete = { nameToDelete ->
                    customCardViewModel.deletePart(part)}
            )
        }
    }
}





