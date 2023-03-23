package com.example.seawatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Composable che rappresenta la prima schermata da visualizzare
 * [onNextButtonClicked] lambda che triggera il cambio di schermata
 */
@Composable
fun FirstScreen(
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "FirstScreen", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onNextButtonClicked() },
            modifier = modifier.widthIn(min = 250.dp)
        ) {
            Text("Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Image(
            painter = painterResource(R.drawable.sea),
            contentDescription = "Immagine Logo"
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "ACCEDI", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(40.dp))
        var text by rememberSaveable { mutableStateOf("") }
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Email") },
            singleLine = true,
            placeholder = { Text("esempio@provider.com") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        var password by rememberSaveable { mutableStateOf("") }
        var passwordHidden by rememberSaveable { mutableStateOf(true) }
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    if (passwordHidden) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_visibility_24),
                            contentDescription = "Visibile"
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                            contentDescription = "Non visibile"
                        )
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(70.dp))
        Button(
            onClick = { onNextButtonClicked() },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer) ,
            modifier = modifier.widthIn(min = 250.dp)
        ) {
            Text("ENTRA")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onNextButtonClicked() },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = modifier.widthIn(min = 180.dp)
        ) {
            Text("CREA ACCOUNT")
        }
    }
}

/**
 * Composable che rappresenta la seconda schermata da visualizzare
 * [onNextButtonClicked] lambda che triggera il cambio di schermata (next)
 * [onCancelButtonClicked] lambda che triggera il cambio di schermata (home)
 */
@Composable
fun SecondScreen(
    onNextButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "SecondScreen", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = { onCancelButtonClicked() },
                modifier = modifier
                    .widthIn(min = 250.dp)
                    .weight(1f)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(2.dp))

            Button(
                onClick = { onNextButtonClicked() },
                modifier = modifier
                    .widthIn(min = 250.dp)
                    .weight(1f)
            ) {
                Text("Next")
            }

        }
    }
}

/**
 * Composable che rappresenta la seconda schermata da visualizzare
 * [onCancelButtonClicked] lambda che triggera il cambio di schermata (home)
 */
@Composable
fun ThirdScreen(
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "ThirdScreen", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onCancelButtonClicked() },
            modifier = modifier.widthIn(min = 250.dp)
        ) {
            Text("Cancel")
        }
    }
}