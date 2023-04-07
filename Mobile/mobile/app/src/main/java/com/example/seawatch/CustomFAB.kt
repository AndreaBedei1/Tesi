package com.example.seawatch

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun CustomFAB(currentScreen:String, navController:NavHostController){
    if(currentScreen == NavigationScreen.Profile.name){ //Forse serve mettere altra condizione && profilo che si sta vedendo è il mio.
        //FAB per modifica del profilo.
        FloatingActionButton(
            shape= RoundedCornerShape(50.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = { navController.navigate(NavigationScreen.ProfileSettings.name) },
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(imageVector = Icons.Filled.Edit, "Modifica profilo")
        }
    } else if(currentScreen == NavigationScreen.Home.name){
        //FAB per aggiugere un avvistamento..
        FloatingActionButton(
            shape= RoundedCornerShape(50.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = { navController.navigate(NavigationScreen.AddSighting.name) },
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
            Icon(imageVector = Icons.Filled.Add, "Edita profilo")
        }
    } else if(currentScreen == NavigationScreen.AddSighting.name || currentScreen == NavigationScreen.AddSightingOffline.name){
        //FAB per confermare l'aggiunta dell'avvistamento.
        /*FloatingActionButton(
            shape= RoundedCornerShape(50.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = { navController.navigate(NavigationScreen.Home.name) },
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
            Icon(imageVector = Icons.Filled.Check, "Conferma aggiunta avvistamento")
        }*/
    }
}