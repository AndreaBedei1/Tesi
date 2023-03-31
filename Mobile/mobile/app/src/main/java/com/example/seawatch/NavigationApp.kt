package com.example.seawatch

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    object AddSighting:NavigationScreen("Aggiungi")
    object Stats: NavigationScreen("Statistiche")
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
    val configuration = LocalConfiguration.current
    var barHeight = 0
    if (configuration.orientation==ORIENTATION_LANDSCAPE){
        barHeight = 50
    } else {
        barHeight = 60
    }
    Scaffold(
        topBar = {
            CustomTopBar(currentScreen=currentScreen, navController=navController, barHeight=barHeight)
        },
        bottomBar = {
            CustomBottomBar(currentScreen, configuration, barHeight, navController)
        },
        floatingActionButton = {
            CustomFAB(currentScreen, navController)
        },
        floatingActionButtonPosition = if(currentScreen == NavigationScreen.Home.name) FabPosition.Center else FabPosition.End,

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
            LoginScreen(
                goToHome = { navigateToHome(navController)},
                goToSignUp = { navController.navigate(NavigationScreen.SignUp.name) }
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
                goToLogin = { navController.navigate(NavigationScreen.LogIn.name) }
            )
        }
        composable(route = NavigationScreen.Home.name){
            HomeScreen()
        }
        composable(route = NavigationScreen.AddSighting.name){
            SightingScreen()
        }
        composable(route = NavigationScreen.Stats.name){
            StatsScreen()
        }
    }
}

private fun navigateToHome(navController: NavHostController) {
    navController.popBackStack(NavigationScreen.LogIn.name, inclusive = true)
    navController.navigate(NavigationScreen.Home.name)
}