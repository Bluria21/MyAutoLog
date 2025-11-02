package com.blruia.mycar.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.blruia.mycar.R
import com.blruia.mycar.domain.CustomCardViewModel
import com.blruia.mycar.domain.EntryViewModel
import com.blruia.mycar.ui.components.CustomButtonLanguage
import com.blruia.mycar.ui.components.CustomButtonTheme
import com.blruia.mycar.ui.components.SettingCustomCard
import com.blruia.mycar.ui.components.SettingIntervals
import com.blruia.mycar.ui.theme.ThemeManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun Avtor(
    navController: NavController,
    themeManager: ThemeManager,
    viewModel: EntryViewModel,
    customCardViewModel: CustomCardViewModel,
    carId: Int
) {
    val isDarkTheme by themeManager.isDarkMode.collectAsState(initial = false)
    val replacementIntervals by viewModel.replacementIntervals.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)


    val context = LocalContext.current
    val activity = context as Activity


    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser
    val isLoggedIn = currentUser != null

    var isColorBlindnessExpanded by remember { mutableStateOf(false) }
    var isIntervalsExpanded by remember { mutableStateOf(false) }
    var showCustomCardDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ChevronLeft,
                contentDescription = "Back",
                tint = colors.onSurface,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { navController.navigate("mainScreen") }
            )
            Text(
                text = stringResource( R.string.menu_settings),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 50.dp),
                color = colors.onSurface,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Тема
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.theme),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface
            )
            CustomButtonTheme(
                isDark = isDarkTheme,
                onToggle = { coroutineScope.launch { themeManager.toggleTheme() } }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface,
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("profile")
                        }
                    }
            )
            CustomButtonLanguage()
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Intervals Card
        SettingIntervals(viewModel)

        HorizontalDivider(
            Modifier.fillMaxWidth().padding(top = 10.dp)
        )

        Button(
            onClick = { showCustomCardDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_new_detail))
        }


        if (showCustomCardDialog) {
            SettingCustomCard(
                carId = carId,
                customCardViewModel = customCardViewModel,
                onDismiss = { showCustomCardDialog = false }
            )
        }

        HorizontalDivider(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            color = colors.onSurface
        )


        Button(
            onClick = {
                if (isLoggedIn) {
                    auth.signOut()
                    googleSignInClient.signOut().addOnCompleteListener {
                        val intent = Intent(activity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        activity.startActivity(intent)
                        activity.finish()
                    }
                } else {
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLoggedIn) Color.Red else Color(0xFF4CAF50) // 🔴 или зелёная
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = if (isLoggedIn)
                    stringResource(R.string.log_out)
                else
                    stringResource(R.string.log_in),
                color = colors.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
