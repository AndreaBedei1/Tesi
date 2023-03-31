package com.example.seawatch

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(currentScreen:String, navController: NavHostController, barHeight:Int){
    if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name) {
        TopAppBar(
            title = { Text(text =  currentScreen, modifier = Modifier.padding(8.dp)) },
            navigationIcon = {
                if(currentScreen != NavigationScreen.Home.name){
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Naviga indietro")
                    }
                }
            },
            modifier = androidx.compose.ui.Modifier.height(barHeight.dp),
            actions = {
                if (currentScreen == NavigationScreen.Settings.name || currentScreen == NavigationScreen.DisplaySettings.name || currentScreen == NavigationScreen.SecuritySettings.name || currentScreen == NavigationScreen.ProfileSettings.name) {
                    IconButton(onClick = { navController.navigate(NavigationScreen.LogIn.name) }) {
                        Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "Esci",
                            tint = Color.Red
                        )
                    }
                } else{
                    if(currentScreen == NavigationScreen.Home.name){
                        IconButton(onClick = {  }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_filter_alt_24), contentDescription = "Filtri")
                        }
                    }
                }
            }
        )
    }
}