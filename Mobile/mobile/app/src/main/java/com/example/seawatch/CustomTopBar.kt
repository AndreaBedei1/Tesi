package com.example.seawatch

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

public var filterPref = false;
public var filterAnima = "";

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(currentScreen:String, navController: NavHostController, barHeight:Int, sharedPrefForLogin:SharedPreferences){
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }
    var favoriteFilter by rememberSaveable { mutableStateOf(false) }
    val options = listOf("", "Animale 1", "Animale 2", "Animale 3", "Animale 4", "Animale 5")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    if(currentScreen != NavigationScreen.LogIn.name && currentScreen != NavigationScreen.SignUp.name) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxHeight().padding(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = currentScreen)
                }
            },
            navigationIcon = {
                if(currentScreen != NavigationScreen.Home.name){
                    Row(
                        modifier = Modifier.fillMaxHeight().padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Naviga indietro")
                        }
                    }
                }
            },
            modifier = Modifier.height(barHeight.dp),
            actions = {
                if (currentScreen == NavigationScreen.Settings.name || currentScreen == NavigationScreen.DisplaySettings.name || currentScreen == NavigationScreen.SecuritySettings.name || currentScreen == NavigationScreen.ProfileSettings.name) {
                    Row(
                        modifier = Modifier.fillMaxHeight().padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            with(sharedPrefForLogin.edit()){
                                putString("USER", "")
                                apply()
                            }
                            navController.navigate(NavigationScreen.LogIn.name) }) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = "Esci",
                                tint = Color.Red
                            )
                        }
                    }
                } else{
                    if(currentScreen == NavigationScreen.Home.name){
                        Row(
                            modifier = Modifier.fillMaxHeight().padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { showFilterDialog = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                                    contentDescription = "Filtri"
                                )
                            }
                        }
                        if (showFilterDialog) {
                            AlertDialog(
                                onDismissRequest = { showFilterDialog = false },
                                title = { Text("Filtri") },
                                text = {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Checkbox(
                                                checked = favoriteFilter,
                                                onCheckedChange = { favoriteFilter = it },
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Text("Solo preferiti")
                                        }
                                        Text("Tipo di animale: ")
                                        Spacer(modifier = Modifier.height(12.dp))
                                        ExposedDropdownMenuBox(
                                            expanded = expanded,
                                            onExpandedChange = { expanded = !expanded },
                                            modifier = Modifier
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.outline,
                                                    RoundedCornerShape(2.dp)
                                                )
                                        ) {
                                            TextField(
                                                // The `menuAnchor` modifier must be passed to the text field for correctness.
                                                modifier = Modifier.menuAnchor(),
                                                readOnly = true,
                                                value = selectedOptionText,
                                                onValueChange = {},
                                                label = { Text("Animale") },
                                                trailingIcon = {
                                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                                        expanded = expanded
                                                    )
                                                },
                                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                            )
                                            ExposedDropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                            ) {
                                                options.forEach { selectionOption ->
                                                    DropdownMenuItem(
                                                        text = { Text(selectionOption) },
                                                        onClick = {
                                                            selectedOptionText = selectionOption
                                                            expanded = false
                                                        },
                                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                    )
                                                }
                                            }
                                        }
                                        Row(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = { showFilterDialog = false }) {
                                                Text("Annulla")
                                            }
                                            TextButton(onClick = { showFilterDialog = false; filterPref=favoriteFilter; filterAnima=selectedOptionText;}) {
                                                Text("Applica")
                                            }
                                        }
                                    }
                                },
                                confirmButton = {/** TODO */}
                            )
                        }
                    }
                }
            }
        )
    }
}


