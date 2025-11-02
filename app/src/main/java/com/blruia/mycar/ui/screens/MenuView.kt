package com.blruia.mycar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blruia.mycar.R
import com.blruia.mycar.di.databasemodule.DataStoreManager
import com.blruia.mycar.domain.CarInfoViewModel
import com.blruia.mycar.domain.EntryViewModel
import com.blruia.mycar.ui.components.ScrollCarView
import com.blruia.mycar.ui.theme.ThemeManager
import kotlinx.coroutines.launch

@Composable
fun MenuView(navController: NavController,
             themeManager: ThemeManager,
             viewModel: CarInfoViewModel,
             carId:Int?,
             dataStoreManager: DataStoreManager,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme

    val cars by viewModel.cars.collectAsState()
    val activeCarId by dataStoreManager.getActiveCarId().collectAsState(initial = null)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerContainerColor = colors.surface,
                modifier = Modifier.drawBehind {
                    val strokeWidth = 5.dp.toPx()
                    val color = colors.onBackground
                    drawLine(
                        color = colors.background,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
                    .clip(RectangleShape),
            ) {
                Text(
                    text = "MyAutoLog",
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold,color = colors.onSurface),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            scope.launch {
                                drawerState.close()
                                navController.navigate("mainScreen")
                            }
                        }
                )
                HorizontalDivider()
                ScrollCarView(
                    navController = navController,
                    cars = cars,
                    activeCarId = activeCarId,
                    onCarClick = { car ->
                        viewModel.setActiveCar(car.id)
                    },
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.profile), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("profile")
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = colors.background,
                        selectedContainerColor = colors.background,
                    )
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_story), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("settingView")
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = colors.background,
                        selectedContainerColor = colors.background,
                    )
                )
                //NavigationDrawerItem(
                //    label = { Text(stringResource(R.string.statistics),fontSize = 20.sp,
                //        fontWeight = FontWeight.Bold,
                //        color = colors.onSurface) },
                //    selected = false,
                //    onClick = {
                //        scope.launch {
                //            drawerState.close()
                //          navController.navigate("statistics")
                //      }
                //  },
                //  colors = NavigationDrawerItemDefaults.colors(
                //      unselectedContainerColor = colors.background,
                //      selectedContainerColor = colors.background,
                //  )
                // )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_settings), fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("infoView")
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = colors.background,
                        selectedContainerColor = colors.background,
                    )
                )
                //    NavigationDrawerItem(
                    //   label = { Text(stringResource(R.string.menu_about),fontSize = 20.sp,
                    //       fontWeight = FontWeight.Bold,
                        //       color = colors.onSurface) },
                    //    selected = false,
                    //    onClick = {
                        //       scope.launch {
                            //          drawerState.close()
                            //          navController.navigate("theapplicationView")
                            //       }
                        //   },
                    //   colors = NavigationDrawerItemDefaults.colors(
                        //       unselectedContainerColor = colors.background,
                        //       selectedContainerColor = colors.background,
                        //    )
                // )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.onBackground)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 15.dp)
                    .zIndex(1f)

            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Меню",
                    tint = colors.onSurface,
                    )
            }
            if (carId != null) {
                activeCarId?.let {
                    val entryViewModel: EntryViewModel = hiltViewModel()
                    IconView(
                        navController = navController,
                        themeManager = themeManager,
                        carId = it,
                        dataStoreManager = dataStoreManager,
                        viewModel = entryViewModel ,
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background)
                        .padding(32.dp),
                ) {
                    Text(
                        text = stringResource(R.string.add_car),
                        color = colors.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Добавить авто",
                        tint = colors.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(top = 15.dp)
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate("profile")
                                }
                            }
                    )
                }
            }
        }
    }
}
