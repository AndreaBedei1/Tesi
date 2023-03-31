package com.example.seawatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.seawatch.ui.theme.SeaWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            SeaWatchTheme(darkTheme = theme == getString(R.string.dark_theme)) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val radioOptions = listOf(getString(R.string.light_theme), getString(R.string.dark_theme))
                    NavigationApp(radioOptions = radioOptions, theme = theme, settingsViewModel =  settingsViewModel)
                }
            }
        }
    }
}
