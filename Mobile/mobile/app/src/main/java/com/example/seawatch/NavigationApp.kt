package com.example.seawatch

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
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: NavigationScreen.LogIn.name

    if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Naviga indietro")
                        }
                    },
                    actions = {
                        if(currentScreen == NavigationScreen.Settings.name) {
                            IconButton(onClick = { navController.navigate(NavigationScreen.LogIn.name) }) {
                                Icon(Icons.Filled.ExitToApp, contentDescription = "Esci", tint = Color.Red)
                            }
                        }
                    }
                )

                /**NavigationAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null, NON CONTROLLATO
                    navigateUp = { navController.navigateUp() },

                )*/
            },
        ) { innerPadding ->
            NavigationGraph(navController, innerPadding)
        }
    } else {
        Scaffold(
        ) { innerPadding ->
            NavigationGraph(navController, innerPadding)
        }
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
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
                goToSecuritySettings = { /*TODO*/ },
                goToProfileSettings = { /*TODO*/ },
                goToDisplaySettings = { /*TODO*/ })
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