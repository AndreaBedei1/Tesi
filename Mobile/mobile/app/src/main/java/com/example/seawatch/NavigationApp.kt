package com.example.seawatch

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class NavigationScreen(val name: String) {
    object Home : NavigationScreen("Home")
    object SignUp: NavigationScreen("SignUp")
    object LogIn : NavigationScreen("LogIn")
    object Second : NavigationScreen("Second")
    object Third : NavigationScreen("Third")
    object Settings : NavigationScreen("Impostazioni")
    object DisplaySettings : NavigationScreen("Impostazioni Schermo")
    object SecuritySettings:NavigationScreen("Impostazioni Sicurezza")
    object ProfileSettings:NavigationScreen("Impostazioni Profilo")
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
    sharedPref: SharedPreferences,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit
) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.LogIn.name
    Scaffold(
        topBar = {
            if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name) {
                TopAppBar(
                    title = { Text(currentScreen) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Naviga indietro")
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
        }
    ) { innerPadding ->
        NavigationGraph(navController, innerPadding, sharedPref=sharedPref, selectedOption=selectedOption, onOptionSelected=onOptionSelected)
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    sharedPref: SharedPreferences,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreen.LogIn.name,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(route = NavigationScreen.LogIn.name) {
            LogInScreen(
                goToHome = {
                    navController.navigate(NavigationScreen.Settings.name)
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
            DisplaySettings(
                sharedPref = sharedPref,
                selectedOption = selectedOption,
                onOptionSelected = onOptionSelected
            )
        }
        composable(route = NavigationScreen.ProfileSettings.name){
            ProfileSettings()
        }
        composable(route = NavigationScreen.SecuritySettings.name) {
            SecuritySettings()
        }
        composable(route = NavigationScreen.SignUp.name) {
            SignUpScreen(
                goToLogin = {
                    navController.navigate(NavigationScreen.LogIn.name)
                }
            )
        }
        composable(route = NavigationScreen.Second.name) {
            SecondScreen(
                onNextButtonClicked = { navController.navigate(NavigationScreen.Third.name) },
                onCancelButtonClicked = {
                    navigateToHome(navController)
                }
            )
        }
        composable(route = NavigationScreen.Third.name) {
            ThirdScreen(
                onCancelButtonClicked = {
                    navigateToHome(navController)
                }
            )
        }
    }
}

private fun navigateToHome(
    navController: NavHostController
) {
    navController.popBackStack(NavigationScreen.Home.name, inclusive = false)
}