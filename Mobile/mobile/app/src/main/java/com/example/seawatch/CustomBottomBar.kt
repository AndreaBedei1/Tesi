package com.example.seawatch

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomBottomBar(currentScreen:String, configuration:Configuration, barHeight:Int, navController:NavHostController){
    if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name){
        BottomAppBar (
            modifier = androidx.compose.ui.Modifier.height(barHeight.dp)
        ){
            // Elemento statistiche nella bottom bar.
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.baseline_bar_chart_24), contentDescription = "Statistiche", modifier = Modifier.size(30.dp)) },
                selected = currentScreen == NavigationScreen.Stats.name,
                onClick = {navController.navigate(NavigationScreen.Stats.name) }
            )

            // Elemento impostazioni nella bottom bar.
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Impostazioni", modifier = Modifier.size(30.dp)) },
                selected = currentScreen == NavigationScreen.Settings.name,
                onClick = {navController.navigate(NavigationScreen.Settings.name) }
            )

            // Elemento home nella bottom bar.
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Homepage", modifier = Modifier.size(30.dp)) },
                selected = currentScreen == NavigationScreen.Home.name,
                onClick = {navController.navigate(NavigationScreen.Home.name) }
            )

            // Elemento profilo nella bottom bar.
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Profilo", modifier = Modifier.size(30.dp)) },
                selected = currentScreen == NavigationScreen.Profile.name,
                onClick = {navController.navigate(NavigationScreen.Profile.name) }
            )
        }
    }
}