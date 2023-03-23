package com.example.seawatch

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class NavigationScreen(val name: String) {
    object Home : NavigationScreen("Home")
    object LogIn : NavigationScreen("LogIn")
    object Second : NavigationScreen("Second")
    object Third : NavigationScreen("Third")
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

    if(currentScreen != NavigationScreen.LogIn.name) {
        Scaffold(
            topBar = {
                NavigationAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
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
                onNextButtonClicked = {
                    navController.navigate(NavigationScreen.Second.name)
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