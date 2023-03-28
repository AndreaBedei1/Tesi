package com.example.seawatch

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class NavigationScreen(val name: String) {
    object Home : NavigationScreen("Home")
    object SignUp: NavigationScreen("SignUp")
    object LogIn : NavigationScreen("LogIn")
    object Settings : NavigationScreen("Impostazioni")
    object DisplaySettings : NavigationScreen("Impostazioni Schermo")
    object SecuritySettings:NavigationScreen("Impostazioni Sicurezza")
    object ProfileSettings:NavigationScreen("Impostazioni Profilo")
    object Profile:NavigationScreen("Profilo")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen) },
        modifier = modifier,
        navigationIcon = {
            //se si può navigare indietro (non home screen) allora appare la freccetta
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationApp(
    navController: NavHostController = rememberNavController(),
    radioOptions: List<String>,
    theme: String,
    settingsViewModel: SettingsViewModel

) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.LogIn.name
    Scaffold(
        topBar = {
            if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name) {
                TopAppBar(
                    title = { Text(text =  currentScreen) },
                    navigationIcon = {
                        if(currentScreen != NavigationScreen.Home.name){
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Naviga indietro")
                            }
                        }
                    },
                    actions = {
                        if (currentScreen == NavigationScreen.Settings.name || currentScreen == NavigationScreen.DisplaySettings.name || currentScreen == NavigationScreen.SecuritySettings.name || currentScreen == NavigationScreen.ProfileSettings.name) {
                            IconButton(onClick = { navController.navigate(NavigationScreen.LogIn.name) }) {
                                Icon(
                                    Icons.Filled.ExitToApp,
                                    contentDescription = "Esci",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                )
            }

            /**NavigationAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null, NON CONTROLLATO
                navigateUp = { navController.navigateUp() },

            )*/
        },
        bottomBar = {
            if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name){
                var selectedItem by remember { mutableStateOf(2) }
                NavigationBar (
                    modifier = Modifier.height(65.dp)
                ){
                    NavigationBarItem(
                        icon = { Icon(painter = painterResource(id = R.drawable.baseline_bar_chart_24), contentDescription = "Statistiche", modifier = Modifier.size(30.dp)) },
                        selected = selectedItem == 0,
                        onClick = { selectedItem = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Impostazioni", modifier = Modifier.size(30.dp)) },
                        selected = currentScreen == NavigationScreen.Settings.name,
                        onClick = {navController.navigate(NavigationScreen.Settings.name) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Homepage", modifier = Modifier.size(30.dp)) },
                        selected = currentScreen == NavigationScreen.Home.name,
                        onClick = {navController.navigate(NavigationScreen.Home.name) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profilo", modifier = Modifier.size(30.dp)) },
                        selected = currentScreen == NavigationScreen.Profile.name,
                        onClick = {navController.navigate(NavigationScreen.Profile.name) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding, radioOptions = radioOptions, theme = theme, settingsViewModel =  settingsViewModel)
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    radioOptions: List<String>,
    theme: String,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.LogIn.name,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(route = NavigationScreen.LogIn.name) {
            LogInScreen(
                goToHome = {
                    navigateToHome(navController)
                },
                goToSignUp = {
                    navController.navigate(NavigationScreen.SignUp.name)
                }
            )
        }
        composable(route = NavigationScreen.Settings.name) {
            Settings(
                goToSecuritySettings = { navController.navigate(NavigationScreen.SecuritySettings.name) },
                goToProfileSettings = { navController.navigate(NavigationScreen.ProfileSettings.name) },
                goToDisplaySettings = { navController.navigate(NavigationScreen.DisplaySettings.name) })
        }
        composable(route = NavigationScreen.DisplaySettings.name) {
            DisplaySettings(radioOptions = radioOptions, theme = theme, settingsViewModel =  settingsViewModel)
        }
        composable(route = NavigationScreen.ProfileSettings.name){
            ProfileSettings()
        }
        composable(route = NavigationScreen.SecuritySettings.name) {
            SecuritySettings()
        }
        composable(route = NavigationScreen.Profile.name) {
            Profile()
        }
        composable(route = NavigationScreen.SignUp.name) {
            SignUpScreen(
                goToLogin = {
                    navController.navigate(NavigationScreen.LogIn.name)
                }
            )
        }
        composable(route = NavigationScreen.Home.name){
            HomeScreen()
        }
    }
}

private fun navigateToHome(
    navController: NavHostController
) {
    navController.popBackStack(NavigationScreen.LogIn.name, inclusive = true)
    navController.navigate(NavigationScreen.Home.name)
}