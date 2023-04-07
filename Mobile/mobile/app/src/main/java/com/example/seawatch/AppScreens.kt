package com.example.seawatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


var ok = true
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    goToHome: () -> Unit,
    goToSignUp:() ->Unit,
    modifier: Modifier = Modifier,
    sharedPrefForLogin:SharedPreferences,
    goToOffline: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var mail by rememberSaveable { mutableStateOf(sharedPrefForLogin.getString("USER", "").toString()) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    val biometricManager = BiometricManager.from(LocalContext.current)
    val executor = ContextCompat.getMainExecutor(LocalContext.current)
    val context = LocalContext.current as FragmentActivity


    if(sharedPrefForLogin.getString("USER", "")!="" && ok ){
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Login con impronta digitale")
                    .setSubtitle("Utilizza l'impronta digitale per accedere")
                    .setDescription("Posiziona il dito sull'impronta digitale")
                    .setNegativeButtonText("Annulla")
                    .build()

                val biometricPrompt = BiometricPrompt(context, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        goToHome()
                        ok = false
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        Toast.makeText(context, "Errore impronta digitale", Toast.LENGTH_LONG).show()
                        ok = false
                    }
                })
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // Il dispositivo non supporta il riconoscimento biometrico
                // Qui si potrebbe mostrare un messaggio di errore o gestire la situazione in modo diverso
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // Il riconoscimento biometrico non è al momento disponibile
                // Qui si potrebbe mostrare un messaggio di errore o gestire la situazione in modo diverso
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Non ci sono impronte digitali registrate sul dispositivo
                // Qui si potrebbe mostrare un messaggio di errore o gestire la situazione in modo diverso
            }
        }
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** Login orizzontale */
            Row (
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(horizontal = hig),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        //Spacer(modifier = Modifier.height(hig))
                        Image(
                            painter = painterResource(R.drawable.sea),
                            contentDescription = "Immagine Logo"
                        )
                    }
                }
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .background(backGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        TextField(
                            value = mail,
                            onValueChange = { mail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            placeholder = { Text("esempio@provider.com") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(min))

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
                            },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                    }
                }
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(horizontal = hig)
                        .background(backGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        Button(
                            onClick = { goToOffline() },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                            modifier = modifier.widthIn(min = 200.dp)
                        ) {
                            Text(" ACCEDI OFFLINE")
                        }
                        Spacer(modifier = Modifier.height(min))
                        Text(text = "oppure", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(min))
                        Button(
                            onClick = {
                                /** TODO Prima controlla se l'utente esiste nel DB online con if */
                                with(sharedPrefForLogin.edit()){
                                    putString("USER", mail)
                                    apply()
                                }
                                ok = false
                                goToHome() },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                            modifier = modifier.widthIn(min = 200.dp)
                        ) {
                            Text("ENTRA")
                        }
                        Spacer(modifier = Modifier.height(min))
                        Button(
                            onClick = { goToSignUp() },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            modifier = modifier.widthIn(min = 150.dp)
                        ) {
                            Text("CREA ACCOUNT")
                        }
                    }
                }
            }
        }
        else -> {                                                   /** Login verticale */
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(hig))
                    Image(
                        painter = painterResource(R.drawable.sea),
                        contentDescription = "Immagine Logo"
                    )
                    Spacer(modifier = Modifier.height(med))
                    Button(
                        onClick = { goToOffline()} ,
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = modifier.widthIn(min = 230.dp)
                    ) {
                        Text(" ACCEDI OFFLINE")
                    }
                    Spacer(modifier = Modifier.height(min/2))
                    Text(text = "oppure", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(min/2))
                    TextField(
                        value = mail,
                        onValueChange = { mail = it },
                        label = { Text("Email") },
                        singleLine = true,
                        placeholder = { Text("esempio@provider.com") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(min))
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
                        },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(med))
                    Button(
                        onClick = {
                            with(sharedPrefForLogin.edit()){
                                putString("USER", mail)
                                apply()
                            }
                            ok = false
                            goToHome() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = modifier.widthIn(min = 230.dp)
                    ) {
                        Text("ENTRA")
                    }
                    Spacer(modifier = Modifier.height(min))
                    Button(
                        onClick = { goToSignUp() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = modifier.widthIn(min = 180.dp)
                    ) {
                        Text("CREA ACCOUNT")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    goToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var mail by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                    /** SignUp Orizzontale */
            Row (
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(horizontal = hig),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        //Spacer(modifier = Modifier.height(hig))
                        Image(
                            painter = painterResource(R.drawable.sea),
                            contentDescription = "Immagine Logo"
                        )
                        Spacer(modifier = Modifier.height(med))
                        Text(text = "REGISTRATI", style = MaterialTheme.typography.titleLarge)
                    }
                }
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .background(backGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nome") },
                            singleLine = true,
                            placeholder = { Text("Mario") },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(min))
                        TextField(
                            value = surname,
                            onValueChange = { surname = it },
                            label = { Text("Cognome") },
                            singleLine = true,
                            placeholder = { Text("Rossi") },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(min))
                        TextField(
                            value = mail,
                            onValueChange = { mail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            placeholder = { Text("esempio@provider.com") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(min))
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
                            },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                    }
                }
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(horizontal = hig)
                        .background(backGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        Button(
                            onClick = { goToLogin() },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                            modifier = modifier.widthIn(min = 200.dp)
                        ) {
                            Text("CREA ACCOUNT")
                        }
                        Spacer(modifier = Modifier.height(min))
                        Button(
                            onClick = { goToLogin() },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            modifier = modifier.widthIn(min = 150.dp)
                        ) {
                            Text("HO UN ACCOUNT")
                        }
                    }
                }
            }
        }
        else -> {                                                   /** SignUp verticale*/
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(med))
                    Image(
                        painter = painterResource(R.drawable.sea),
                        contentDescription = "Immagine Logo"
                    )
                    Spacer(modifier = Modifier.height(min))
                    Text(text = "REGISTRATI", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(min))
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") },
                        singleLine = true,
                        placeholder = { Text("Mario") },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(min/2))
                    TextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text("Cognome") },
                        singleLine = true,
                        placeholder = { Text("Rossi") },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(min/2))
                    TextField(
                        value = mail,
                        onValueChange = { mail = it },
                        label = { Text("Email") },
                        singleLine = true,
                        placeholder = { Text("esempio@provider.com") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(min/2))
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
                        },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(med))
                    Button(
                        onClick = { goToLogin() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = modifier.widthIn(min = 250.dp)
                    ) {
                        Text("CREA ACCOUNT")
                    }
                    Spacer(modifier = Modifier.height(min))
                    Button(
                        onClick = { goToLogin() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = modifier.widthIn(min = 180.dp)
                    ) {
                        Text("HO UN ACCOUNT")
                    }
                }
            }
        }
    }
}

/**
 * Composable che rappresenta la seconda schermata da visualizzare
 * [onCancelButtonClicked] lambda che triggera il cambio di schermata (home)
 */
@Composable
fun Settings(
    goToSecuritySettings: () -> Unit,
    goToProfileSettings: () -> Unit,
    goToDisplaySettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val width = configuration.screenWidthDp.dp-30.dp
    val backGround = MaterialTheme.colorScheme.primaryContainer
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(backGround),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(1) { element ->
            Spacer(modifier = Modifier.height(min))
            Button(
                onClick = { goToProfileSettings() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = modifier.widthIn(min = width)
            ) {
                Text("PROFILO")
            }
            Spacer(modifier = Modifier.height(min))
            Button(
                onClick = { goToDisplaySettings() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = modifier.widthIn(min = width)
            ) {
                Text("DISPLAY")
            }
            Spacer(modifier = Modifier.height(min))
            Button(
                onClick = { goToSecuritySettings() },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = modifier.widthIn(min = width)
            ) {
                Text("SICUREZZA")
            }
        }
    }
}

@Composable
fun DisplaySettings(
    modifier: Modifier = Modifier,
    radioOptions: List<String>,
    theme: String,
    settingsViewModel: SettingsViewModel
) {
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup().background(MaterialTheme.colorScheme.primaryContainer).fillMaxSize()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == theme),
                        onClick = {
                            settingsViewModel.saveTheme(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == theme),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecuritySettings(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val width = configuration.screenWidthDp.dp-30.dp
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var oldPasswordHidden by rememberSaveable { mutableStateOf(true) }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var newPasswordHidden by rememberSaveable { mutableStateOf(true) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(backGround),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(1) { element ->
            TextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                singleLine = true,
                label = { Text("Vecchia password") },
                visualTransformation =
                if (oldPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { oldPasswordHidden = !oldPasswordHidden }) {
                        if (oldPasswordHidden) {
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
                },
                modifier = Modifier.background(backGround),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            Spacer(modifier = Modifier.height(min))
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                singleLine = true,
                label = { Text("Nuova assword") },
                visualTransformation =
                if (newPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { newPasswordHidden = !newPasswordHidden }) {
                        if (newPasswordHidden) {
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
                },
                modifier = Modifier.background(backGround),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            Spacer(modifier = Modifier.height(min))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                label = { Text("Conferma password") },
                visualTransformation =
                if (confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                        if (confirmPasswordHidden) {
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
                },
                modifier = Modifier.background(backGround),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            Spacer(modifier = Modifier.height(med))
            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = modifier.widthIn(min = 250.dp)
            ) {
                Text("AGGIORNA PASSWRD")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettings(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var nome by rememberSaveable { mutableStateOf("") }
    var cognome by rememberSaveable { mutableStateOf("") }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** profile orizzontale */
            Row (
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .size(
                            width = configuration.screenWidthDp.dp / 2,
                            height = configuration.screenHeightDp.dp
                        )
                        .padding(horizontal = hig),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        Image(
                            painter = painterResource(R.drawable.sea),
                            contentDescription = "Immagine Profilo"
                        )
                        Spacer(modifier = Modifier.height(med))
                        Button(
                            onClick = { /** TODO */ },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            modifier = modifier.widthIn(min = 150.dp)
                        ) {
                            Text("CAMBIA FOTO")
                        }
                        Spacer(modifier = Modifier.height(med))
                    }
                }
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .background(backGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        Spacer(modifier = Modifier.height(min-5.dp))
                        TextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome") },
                            singleLine = true,
                            placeholder = { Text("Mario") },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(min))
                        TextField(
                            value = cognome,
                            onValueChange = { cognome = it },
                            label = { Text("Cognome") },
                            singleLine = true,
                            placeholder = { Text("Rossi") },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors()
                        )
                        Spacer(modifier = Modifier.height(hig+15.dp))
                        Button(
                            onClick = { /** TODO */ },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            modifier = modifier.widthIn(min = 200.dp)
                        ) {
                            Text("AGGIORNA")
                        }
                    }
                }
            }
        }
        else -> {                                                   /** profile verticale */
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(hig))
                    Image(
                        painter = painterResource(R.drawable.sea),
                        contentDescription = "Immagine Profilo"
                    )
                    Spacer(modifier = Modifier.height(min))
                    Button(
                        onClick = { /** TODO */ },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = modifier.widthIn(min = 150.dp)
                    ) {
                        Text("CAMBIA FOTO")
                    }
                    Spacer(modifier = Modifier.height(med))
                    TextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        singleLine = true,
                        placeholder = { Text("Mario") },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(min))
                    TextField(
                        value = cognome,
                        onValueChange = { cognome = it },
                        label = { Text("Cognome") },
                        singleLine = true,
                        placeholder = { Text("Rossi") },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors()
                    )
                    Spacer(modifier = Modifier.height(med))
                    Button(
                        onClick = { /** TODO */ },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        modifier = modifier.widthIn(min = 200.dp)
                    ) {
                        Text("AGGIORNA")
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** Login orizzontale */
            Row (
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .size(
                            width = configuration.screenWidthDp.dp / 3,
                            height = configuration.screenHeightDp.dp
                        )
                        .padding(horizontal = hig),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        Image(
                            painter = painterResource(R.drawable.sea),
                            contentDescription = "Immagine Profilo"
                        )
                    }
                }
                LazyColumn(
                    modifier = modifier
                        .fillMaxHeight()
                        .background(backGround),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    items(1) { element ->
                        Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center){
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.width((configuration.screenWidthDp/3).dp)) {
                                Spacer(modifier = Modifier.height(min))
                                Text(text="Nome:", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(min))
                                Text(text="Cognome:", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(min))
                                Text(text="Mail:", style = MaterialTheme.typography.titleLarge)
                            }
                            Column(horizontalAlignment = Alignment.Start, modifier=Modifier.width((configuration.screenWidthDp/3).dp)) {
                                Spacer(modifier = Modifier.height(min+5.dp))
                                Text(text="Mario", style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(min+6.dp))
                                Text(text="Rossi", style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(min+6.dp))
                                Text(text="andreabedei@libero.it", textDecoration = TextDecoration.Underline, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
        else -> {                                                   /** Login verticale */
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(hig))
                    Image(
                        painter = painterResource(R.drawable.sea),
                        contentDescription = "Immagine Profilo"
                    )
                    Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.width((configuration.screenWidthDp/2).dp)) {
                            Spacer(modifier = Modifier.height(min))
                            Text(text="Nome:", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(min))
                            Text(text="Cognome:", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(min))
                            Text(text="Mail:", style = MaterialTheme.typography.titleLarge)
                        }
                        Column(horizontalAlignment = Alignment.Start, modifier=Modifier.width((configuration.screenWidthDp/2).dp)) {
                            Spacer(modifier = Modifier.height(min+5.dp))
                            Text(text="Mario", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(min+6.dp))
                            Text(text="Rossi", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(min+6.dp))
                            Text(text="andreabedei@libero.it", textDecoration = TextDecoration.Underline, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToSighting: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp / 40
    val med = configuration.screenHeightDp.dp / 20
    val hig = configuration.screenHeightDp.dp / 10
    val backGround = MaterialTheme.colorScheme.primaryContainer

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            /** Homepage Orizzontale */
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.width((configuration.screenWidthDp/2.3).dp)) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    // Imposta le opzioni WebView necessarie
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                }
                            },
                            update = { webView ->
                                // Carica la mappa Leaflet nel WebView
                                webView.loadUrl("file:///android_asset/leaflet/index.html")
                            }
                        )
                    }
                }
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(20){element->
                        Spacer(modifier = Modifier.height(min/2))
                        Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                            Column() {
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(width = 180.dp, height = 150.dp),
                                    border= BorderStroke(2.dp,Color.Black),
                                    colors = CardDefaults.outlinedCardColors(),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    onClick = {goToSighting()}
                                ) {
                                    var isFavorite by remember { mutableStateOf(false) } /** Cambiare in base al DB */
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_supervised_user_circle_24),
                                                contentDescription = "User Image",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(med+50.dp))
                                            IconButton(
                                                onClick = { isFavorite = !isFavorite  },
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    contentDescription = "Favorite",
                                                    tint = if (isFavorite) Color.Red else LocalContentColor.current
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = "Data",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Animale",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Utente",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                            Column() {
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(width = 180.dp, height = 150.dp),
                                    border= BorderStroke(2.dp,Color.Black),
                                    colors = CardDefaults.outlinedCardColors(),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    onClick = {goToSighting()}
                                ) {
                                    var isFavorite by remember { mutableStateOf(false) } /** Cambiare in base al DB */
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_supervised_user_circle_24),
                                                contentDescription = "User Image",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(med+50.dp))
                                            IconButton(
                                                onClick = { isFavorite = !isFavorite  },
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    contentDescription = "Favorite",
                                                    tint = if (isFavorite) Color.Red else LocalContentColor.current
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Text(
                                            text = "Data",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Animale",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Utente",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else -> {
            /** Homepage verticale*/
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(min))
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    // Imposta le opzioni WebView necessarie
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                }
                            },
                            update = { webView ->
                                // Carica la mappa Leaflet nel WebView
                                webView.loadUrl("file:///android_asset/leaflet/index.html")
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(min))
                    Text(
                        text = "Avvistamenti Online",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(min/2))
                    for(i in 1..20){
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                            Column(modifier=Modifier.width((configuration.screenWidthDp/2).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    border= BorderStroke(2.dp,Color.Black),
                                    colors = CardDefaults.outlinedCardColors(),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    onClick = {goToSighting()}

                                ) {
                                    var isFavorite by remember { mutableStateOf(false) } /** Cambiare in base al DB */
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_supervised_user_circle_24),
                                                contentDescription = "User Image",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(med+30.dp))
                                            IconButton(
                                                onClick = { isFavorite = !isFavorite  },
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    contentDescription = "Favorite",
                                                    tint = if (isFavorite) Color.Red else LocalContentColor.current
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "Data",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Animale",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Utente",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                            Column(modifier=Modifier.width((configuration.screenWidthDp/2).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    border= BorderStroke(2.dp,Color.Black),
                                    colors = CardDefaults.outlinedCardColors(),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    onClick = {goToSighting()}
                                ) {
                                    var isFavorite by remember { mutableStateOf(false) } /** Cambiare in base al DB */
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource(R.drawable.baseline_supervised_user_circle_24),
                                                contentDescription = "User Image",
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(med+30.dp))
                                            IconButton(
                                                onClick = { isFavorite = !isFavorite  },
                                                modifier = Modifier.align(Alignment.CenterVertically)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Favorite,
                                                    contentDescription = "Favorite",
                                                    tint = if (isFavorite) Color.Red else LocalContentColor.current
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(13.dp))
                                        Text(
                                            text = "Data",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Animale",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Utente",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingScreen(
    modifier: Modifier = Modifier,

) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var utente by rememberSaveable { mutableStateOf("Mario Rossi") }
    val currentDateTimeClock = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ITALIAN)
    val formattedDateTime = currentDateTimeClock.format(formatter)
    var data by rememberSaveable { mutableStateOf(formattedDateTime) }
    var numeroEsemplari by rememberSaveable { mutableStateOf("") }
    var posizione by rememberSaveable { mutableStateOf("") }
    var mare by rememberSaveable { mutableStateOf("") }
    var vento by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    val options = listOf("Animale 1", "Animale 2", "Animale 3", "Animale 4", "Animale 5")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    val optionsSpecie = listOf("Specie 1", "Specie 2", "Specie 3", "Specie 4", "Specie 5")
    var expandedSpecie by rememberSaveable { mutableStateOf(false) }
    var selectedOptionTextSpecie by rememberSaveable { mutableStateOf("") }
    var showFilterInfoSpecie by rememberSaveable { mutableStateOf(false) }
    val contex = LocalContext.current
    val currentDateTime by rememberSaveable {mutableStateOf( System.currentTimeMillis().toString())}
    var count by rememberSaveable {mutableStateOf(0)}
    var imagesList =(contex as MainActivity).getAllSavedImages(currentDateTime.toString())

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** Login orizzontale */
            Column(modifier=Modifier.background(backGround)) {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = hig),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            items(1) { element ->
                                Row() {
                                    Column(
                                        modifier = Modifier.width(configuration.screenWidthDp.dp / 2)
                                    ) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Row() {
                                            Column() {
                                                Text(
                                                    text = "Utente:",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = "Data:",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            }
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = utente,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    text = data,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }
                                        // Numero Esemplari
                                        OutlinedTextField(
                                            value = numeroEsemplari,
                                            onValueChange = { numeroEsemplari = it },
                                            label = { Text("Numero Esemplari") },
                                            keyboardOptions = KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Number
                                            ),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Row(
                                        ) {
                                            OutlinedTextField(
                                                value = posizione,
                                                onValueChange = { posizione = it },
                                                label = { Text("Posizione") },
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    keyboardType = KeyboardType.Decimal
                                                ),
                                                singleLine = true,
                                                trailingIcon = {
                                                    IconButton(onClick = { }) {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.baseline_gps_fixed_24),
                                                            contentDescription = "GPS",
                                                            tint = Color.Black
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        // Mare
                                        OutlinedTextField(
                                            value = mare,
                                            onValueChange = { mare = it },
                                            label = { Text("Mare") },
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                        )
                                        // Vento
                                        OutlinedTextField(
                                            value = vento,
                                            onValueChange = { vento = it },
                                            label = { Text("Vento") },
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                        )
                                    }
                                    Column(modifier = Modifier.width(configuration.screenWidthDp.dp / 2)) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        ExposedDropdownMenuBox(
                                            expanded = expanded,
                                            onExpandedChange = { expanded = !expanded },
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
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
                                        Spacer(modifier = Modifier.height(6.dp))
                                        // Specie
                                        Row {
                                            ExposedDropdownMenuBox(
                                                modifier = Modifier
                                                    .width(245.dp)
                                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                                    .border(
                                                        1.dp,
                                                        MaterialTheme.colorScheme.outline,
                                                        RoundedCornerShape(2.dp)
                                                    ),
                                                expanded = expandedSpecie,
                                                onExpandedChange = { expandedSpecie = !expandedSpecie },
                                            ) {
                                                TextField(
                                                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                                                    modifier = Modifier.menuAnchor(),
                                                    readOnly = true,
                                                    value = selectedOptionTextSpecie,
                                                    onValueChange = {},
                                                    label = { Text("Specie") },
                                                    trailingIcon = {
                                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                                            expanded = expandedSpecie
                                                        )
                                                    },
                                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = expandedSpecie,
                                                    onDismissRequest = { expanded = false },
                                                ) {
                                                    optionsSpecie.forEach { selectionOptionSpecie ->
                                                        DropdownMenuItem(
                                                            text = { Text(selectionOptionSpecie) },
                                                            onClick = {
                                                                selectedOptionTextSpecie =
                                                                    selectionOptionSpecie
                                                                expandedSpecie = false
                                                            },
                                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                        )
                                                    }
                                                }
                                            }
                                            Button(
                                                modifier = Modifier
                                                    .size(35.dp)
                                                    .padding(0.dp)
                                                    .align(Alignment.CenterVertically),
                                                colors = ButtonDefaults.buttonColors(
                                                    MaterialTheme.colorScheme.secondaryContainer,
                                                    MaterialTheme.colorScheme.primary
                                                ),
                                                contentPadding = PaddingValues(0.dp),
                                                onClick = { showFilterInfoSpecie = true }) {
                                                Icon(
                                                    modifier = Modifier.fillMaxSize(),
                                                    imageVector = Icons.Filled.Info,
                                                    contentDescription = "Vedi dettagli specie"
                                                )
                                            }
                                            InfoSpecie(showFilterInfoSpecie =  showFilterInfoSpecie){
                                                showFilterInfoSpecie = false
                                            }
                                        }
                                        // Note
                                        OutlinedTextField(
                                            value = note,
                                            onValueChange = { note = it },
                                            label = { Text("Note") }
                                        )

                                        Button(
                                            modifier = Modifier
                                                .padding(vertical = 16.dp)
                                                .align(Alignment.CenterHorizontally),
                                            onClick = {
                                                (contex as MainActivity).requestCameraPermission(currentDateTime.toString(), count)
                                                count+=1
                                            },
                                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                        ) {
                                            Icon(painterResource(id =R.drawable.baseline_camera_alt_24), contentDescription = "Foto")
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(text = "AGGIUNGI")
                                        }

                                    }
                                }
                                Row(horizontalArrangement = Arrangement.Center){
                                    showImages(imagesUri = imagesList, contex)
                                }
                            }
                        }
                    }
                }
            }
        }
        else -> {
            LazyColumn(modifier=Modifier.background(backGround).fillMaxSize()){
                items(1) { element ->
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize(),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        /** Login verticale */
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row() {
                                Column() {
                                    Text(
                                        text = "Utente:",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "Data:",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = utente,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(text = data, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                            // Numero Esemplari
                            OutlinedTextField(
                                value = numeroEsemplari,
                                onValueChange = { numeroEsemplari = it },
                                label = { Text("Numero Esemplari") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = posizione,
                                    onValueChange = { posizione = it },
                                    label = { Text("Posizione") },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = { }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_gps_fixed_24),
                                                contentDescription = "GPS",
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            //Animali
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
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
                            Spacer(modifier = Modifier.height(8.dp))
                            // Specie
                            Row {
                                ExposedDropdownMenuBox(
                                    modifier = Modifier
                                        .width(245.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            RoundedCornerShape(2.dp)
                                        ),
                                    expanded = expandedSpecie,
                                    onExpandedChange = { expandedSpecie = !expandedSpecie },
                                ) {
                                    TextField(
                                        // The `menuAnchor` modifier must be passed to the text field for correctness.
                                        modifier = Modifier.menuAnchor(),
                                        readOnly = true,
                                        value = selectedOptionTextSpecie,
                                        onValueChange = {},
                                        label = { Text("Specie") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = expandedSpecie
                                            )
                                        },
                                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expandedSpecie,
                                        onDismissRequest = { expandedSpecie = false },
                                    ) {
                                        optionsSpecie.forEach { selectionOptionSpecie ->
                                            DropdownMenuItem(
                                                text = {Text(selectionOptionSpecie)},
                                                onClick = {
                                                    selectedOptionTextSpecie = selectionOptionSpecie
                                                    expandedSpecie = false
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            )
                                        }
                                    }
                                }
                                Button(
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(0.dp)
                                        .align(Alignment.CenterVertically),
                                    colors = ButtonDefaults.buttonColors(
                                        MaterialTheme.colorScheme.secondaryContainer,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    onClick = { showFilterInfoSpecie = true }) {
                                    Icon(
                                        modifier = Modifier.fillMaxSize(),
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = "Vedi dettagli specie"
                                    )
                                }
                                InfoSpecie(showFilterInfoSpecie =  showFilterInfoSpecie){
                                    showFilterInfoSpecie = false
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            // Mare
                            OutlinedTextField(
                                value = mare,
                                onValueChange = { mare = it },
                                label = { Text("Mare") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                            // Vento
                            OutlinedTextField(
                                value = vento,
                                onValueChange = { vento = it },
                                label = { Text("Vento") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )

                            // Note
                            OutlinedTextField(
                                value = note,
                                onValueChange = { note = it },
                                label = { Text("Note") }
                            )

                            Button(
                                modifier = Modifier.padding(vertical = 16.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                onClick = {
                                    (contex as MainActivity).requestCameraPermission(currentDateTime.toString(), count)
                                    count+=1
                                }
                            ) {
                                Icon(painterResource(id =R.drawable.baseline_camera_alt_24), contentDescription = "Foto")
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "AGGIUNGI")
                            }
                            showImages(imagesUri = imagesList, contex)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun showImages( imagesUri: List<Uri>?, context: Context) {
    val uris = imagesUri ?: emptyList()
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        for (uri in uris) {
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column {
                        val painter = rememberImagePainter(uri.toString())
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Row(horizontalArrangement = Arrangement.Center){
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.setDataAndType(uri, "image/*")
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                }
                            ) {
                                Text("VISUALIZZA")
                            }
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                                onClick = {
                                    context.contentResolver.delete(uri, null, null)
                                    recreate((context as Activity))
                                }
                            ) {
                                Text("ELIMINA")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingScreenOffline(
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var utente by rememberSaveable { mutableStateOf("Sconosciuto") }
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ITALIAN)
    val formattedDateTime = currentDateTime.format(formatter)
    var data by rememberSaveable { mutableStateOf(formattedDateTime) }
    var numeroEsemplari by rememberSaveable { mutableStateOf("") }
    var posizione by rememberSaveable { mutableStateOf("") }
    var mare by rememberSaveable { mutableStateOf("") }
    var vento by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    val options = listOf("Animale 1", "Animale 2", "Animale 3", "Animale 4", "Animale 5")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    val optionsSpecie = listOf("Specie 1", "Specie 2", "Specie 3", "Specie 4", "Specie 5")
    var expandedSpecie by rememberSaveable { mutableStateOf(false) }
    var selectedOptionTextSpecie by rememberSaveable { mutableStateOf("") }
    val contex = LocalContext.current
    val cu by rememberSaveable {mutableStateOf( System.currentTimeMillis().toString())}
    var count by rememberSaveable {mutableStateOf(0)}
    var imagesList =(contex as MainActivity).getAllSavedImages(cu.toString())


    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** Login orizzontale */
            Column(modifier=Modifier.background(backGround)) {
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = hig),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            items(1) { element ->
                                Row() {
                                    Column(
                                        modifier = Modifier.width(configuration.screenWidthDp.dp / 2)
                                    ) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Row() {
                                            Column() {
                                                Text(
                                                    text = "Utente:",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    text = "Data:",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            }
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = utente,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    text = data,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }
                                        // Numero Esemplari
                                        OutlinedTextField(
                                            value = numeroEsemplari,
                                            onValueChange = { numeroEsemplari = it },
                                            label = { Text("Numero Esemplari") },
                                            keyboardOptions = KeyboardOptions.Default.copy(
                                                keyboardType = KeyboardType.Number
                                            ),
                                            singleLine = true
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Row(
                                        ) {
                                            OutlinedTextField(
                                                value = posizione,
                                                onValueChange = { posizione = it },
                                                label = { Text("Posizione") },
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    keyboardType = KeyboardType.Decimal
                                                ),
                                                singleLine = true,
                                                trailingIcon = {
                                                    IconButton(onClick = { }) {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.baseline_gps_fixed_24),
                                                            contentDescription = "GPS",
                                                            tint = Color.Black
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        // Mare
                                        OutlinedTextField(
                                            value = mare,
                                            onValueChange = { mare = it },
                                            label = { Text("Mare") },
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                        )
                                        // Vento
                                        OutlinedTextField(
                                            value = vento,
                                            onValueChange = { vento = it },
                                            label = { Text("Vento") },
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    Column(modifier = Modifier.width(configuration.screenWidthDp.dp / 2)) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        ExposedDropdownMenuBox(
                                            expanded = expanded,
                                            onExpandedChange = { expanded = !expanded },
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
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
                                        Spacer(modifier = Modifier.height(6.dp))
                                        // Specie
                                        ExposedDropdownMenuBox(
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.outline,
                                                    RoundedCornerShape(2.dp)
                                                ),
                                            expanded = expandedSpecie,
                                            onExpandedChange = { expandedSpecie = !expandedSpecie },
                                        ) {
                                            TextField(
                                                // The `menuAnchor` modifier must be passed to the text field for correctness.
                                                modifier = Modifier.menuAnchor(),
                                                readOnly = true,
                                                value = selectedOptionTextSpecie,
                                                onValueChange = {},
                                                label = { Text("Specie") },
                                                trailingIcon = {
                                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                                        expanded = expandedSpecie
                                                    )
                                                },
                                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                            )
                                            ExposedDropdownMenu(
                                                expanded = expandedSpecie,
                                                onDismissRequest = { expanded = false },
                                            ) {
                                                optionsSpecie.forEach { selectionOptionSpecie ->
                                                    DropdownMenuItem(
                                                        text = { Text(selectionOptionSpecie) },
                                                        onClick = {
                                                            selectedOptionTextSpecie =
                                                                selectionOptionSpecie
                                                            expandedSpecie = false
                                                        },
                                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                    )
                                                }
                                            }
                                        }
                                        // Note
                                        OutlinedTextField(
                                            value = note,
                                            onValueChange = { note = it },
                                            label = { Text("Note") }
                                        )

                                        // Submit button
                                        Button(
                                            modifier = Modifier
                                                .padding(vertical = 16.dp)
                                                .align(Alignment.CenterHorizontally),
                                            onClick = {
                                                (contex as MainActivity).requestCameraPermission(cu.toString(), count)
                                                count+=1
                                            },
                                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),

                                            ) {
                                            Icon(painterResource(id =R.drawable.baseline_camera_alt_24), contentDescription = "Foto")
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(text = "AGGIUNGI")
                                        }
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.Center){
                                    showImages(imagesUri = imagesList, contex)
                                }
                            }
                        }
                    }
                }
            }
        }
        else -> {
            Scaffold(floatingActionButton={
                FloatingActionButton(
                    shape= RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { /** navController.navigate(NavigationScreen.ProfileSettings.name)*/ },
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(imageVector = Icons.Filled.Edit, "Modifica profilo")
                }
            }, floatingActionButtonPosition = FabPosition.End){innerPadding->
                LazyColumn(modifier= Modifier.padding(innerPadding)
                    .background(backGround)
                    .fillMaxSize()){
                    items(1) { element ->
                        Card(
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize(),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            /** Login verticale */
                            Column(
                                modifier = modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row() {
                                    Column() {
                                        Text(
                                            text = "Utente:",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Data:",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = utente,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(text = data, style = MaterialTheme.typography.bodyLarge)
                                    }
                                }
                                // Numero Esemplari
                                OutlinedTextField(
                                    value = numeroEsemplari,
                                    onValueChange = { numeroEsemplari = it },
                                    label = { Text("Numero Esemplari") },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = posizione,
                                        onValueChange = { posizione = it },
                                        label = { Text("Posizione") },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        trailingIcon = {
                                            IconButton(onClick = { }) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_gps_fixed_24),
                                                    contentDescription = "GPS",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                //Animali
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
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
                                Spacer(modifier = Modifier.height(8.dp))
                                // Specie
                                ExposedDropdownMenuBox(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline,
                                            RoundedCornerShape(2.dp)
                                        ),
                                    expanded = expandedSpecie,
                                    onExpandedChange = { expandedSpecie = !expandedSpecie },
                                ) {
                                    TextField(
                                        // The `menuAnchor` modifier must be passed to the text field for correctness.
                                        modifier = Modifier.menuAnchor(),
                                        readOnly = true,
                                        value = selectedOptionTextSpecie,
                                        onValueChange = {},
                                        label = { Text("Specie") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = expandedSpecie
                                            )
                                        },
                                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expandedSpecie,
                                        onDismissRequest = { expandedSpecie = false },
                                    ) {
                                        optionsSpecie.forEach { selectionOptionSpecie ->
                                            DropdownMenuItem(
                                                text = {Text(selectionOptionSpecie)},
                                                onClick = {
                                                    selectedOptionTextSpecie = selectionOptionSpecie
                                                    expandedSpecie = false
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(3.dp))
                                // Mare
                                OutlinedTextField(
                                    value = mare,
                                    onValueChange = { mare = it },
                                    label = { Text("Mare") },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )
                                // Vento
                                OutlinedTextField(
                                    value = vento,
                                    onValueChange = { vento = it },
                                    label = { Text("Vento") },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )

                                // Note
                                OutlinedTextField(
                                    value = note,
                                    onValueChange = { note = it },
                                    label = { Text("Note") }
                                )

                                // Submit button
                                Button(
                                    onClick = {
                                        (contex as MainActivity).requestCameraPermission(cu.toString(), count)
                                        count+=1
                                    },
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                ) {
                                    Icon(painterResource(id =R.drawable.baseline_camera_alt_24), contentDescription = "Foto")
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = "AGGIUNGI")
                                }
                                showImages(imagesUri = imagesList, contex)





                            }
                        }
                    }
                }
            }

        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "Mettere le statistiche")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingViewScreen(
    modifier: Modifier = Modifier,
    owner: Boolean
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var utente by remember { mutableStateOf("Mario Rossi") }
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ITALIAN)
    val formattedDateTime = currentDateTime.format(formatter)
    var data by rememberSaveable { mutableStateOf(formattedDateTime) }
    var numeroEsemplari by rememberSaveable { mutableStateOf("3") }
    var posizione by rememberSaveable { mutableStateOf("3") }
    var mare by rememberSaveable { mutableStateOf("3") }
    var vento by rememberSaveable { mutableStateOf("3") }
    var note by rememberSaveable { mutableStateOf("3") }
    val options = listOf("Animale 1", "Animale 2", "Animale 3", "Animale 4", "Animale 5")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("Animale 1") }
    val optionsSpecie = listOf("Specie 1", "Specie 2", "Specie 3", "Specie 4", "Specie 5")
    var expandedSpecie by rememberSaveable { mutableStateOf(false) }
    var selectedOptionTextSpecie by rememberSaveable { mutableStateOf("Specie 1") }
    var showFilterInfoSpecie by rememberSaveable { mutableStateOf(false) }
    val contex = LocalContext.current
    val cu by rememberSaveable {mutableStateOf( System.currentTimeMillis().toString())}
    var count by rememberSaveable {mutableStateOf(0)}
    var imagesList =(contex as MainActivity).getAllSavedImages(cu.toString())


    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** SightingViewScreen orizzontale */
            Column(modifier=Modifier.background(backGround)) {
                if(owner){
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(backGround)
                    ) {
                        Column(
                            modifier = modifier
                                .width(configuration.screenWidthDp.dp / 2)
                                .padding(horizontal = hig),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        WebView(context).apply {
                                            // Imposta le opzioni WebView necessarie
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true
                                        }
                                    },
                                    update = { webView ->
                                        // Carica la mappa Leaflet nel WebView
                                        webView.loadUrl("file:///android_asset/leaflet/index.html")
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier.width(configuration.screenWidthDp.dp / 2)
                        ) {
                            items(1) { element ->
                                Row() {
                                    Column() {
                                        Text(
                                            text = "Utente:",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Data:",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = utente,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = data,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                                // Numero Esemplari
                                OutlinedTextField(
                                    value = numeroEsemplari,
                                    onValueChange = { numeroEsemplari = it },
                                    label = { Text("Numero Esemplari") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(
                                ) {
                                    OutlinedTextField(
                                        value = posizione,
                                        onValueChange = { posizione = it },
                                        label = { Text("Posizione") },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Decimal
                                        ),
                                        singleLine = true,
                                        trailingIcon = {
                                            IconButton(onClick = { }) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_gps_fixed_24),
                                                    contentDescription = "GPS",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                // Mare
                                OutlinedTextField(
                                    value = mare,
                                    onValueChange = { mare = it },
                                    label = { Text("Mare") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                                // Vento
                                OutlinedTextField(
                                    value = vento,
                                    onValueChange = { vento = it },
                                    label = { Text("Vento") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primaryContainer)
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
                                                    selectedOptionText =
                                                        selectionOption
                                                    expanded = false
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                // Specie
                                Row {
                                    ExposedDropdownMenuBox(
                                        modifier = Modifier
                                            .width(245.dp)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.outline,
                                                RoundedCornerShape(2.dp)
                                            ),
                                        expanded = expandedSpecie,
                                        onExpandedChange = {
                                            expandedSpecie = !expandedSpecie
                                        },
                                    ) {
                                        TextField(
                                            // The `menuAnchor` modifier must be passed to the text field for correctness.
                                            modifier = Modifier.menuAnchor(),
                                            readOnly = true,
                                            value = selectedOptionTextSpecie,
                                            onValueChange = {},
                                            label = { Text("Specie") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(
                                                    expanded = expandedSpecie
                                                )
                                            },
                                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedSpecie,
                                            onDismissRequest = { expanded = false },
                                        ) {
                                            optionsSpecie.forEach { selectionOptionSpecie ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            selectionOptionSpecie
                                                        )
                                                    },
                                                    onClick = {
                                                        selectedOptionTextSpecie =
                                                            selectionOptionSpecie
                                                        expandedSpecie = false
                                                    },
                                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                )
                                            }
                                        }
                                    }
                                    Button(
                                        modifier = Modifier
                                            .size(35.dp)
                                            .padding(0.dp)
                                            .align(Alignment.CenterVertically),
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.primary
                                        ),
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = { showFilterInfoSpecie = true }) {
                                        Icon(
                                            modifier = Modifier.fillMaxSize(),
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = "Vedi dettagli specie"
                                        )
                                    }
                                    InfoSpecie(showFilterInfoSpecie =  showFilterInfoSpecie){
                                        showFilterInfoSpecie = false
                                    }
                                }
                                // Note
                                OutlinedTextField(
                                    value = note,
                                    onValueChange = { note = it },
                                    label = { Text("Note") }
                                )

                                Row() {
                                    // Submit button
                                    Button(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp),
                                        onClick = {
                                            (contex as MainActivity).requestCameraPermission(cu.toString(), count)
                                            count+=1
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.primary
                                        ),

                                        ) {
                                        Icon(
                                            painterResource(id = R.drawable.baseline_camera_alt_24),
                                            contentDescription = "Foto"
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(text = "AGGIUNGI")
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Button(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp),
                                        onClick = {

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.primary
                                        ),

                                        ) {
                                        Icon(
                                            painterResource(id = R.drawable.baseline_save_24),
                                            contentDescription = "Salva"
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(text = "SALVA")
                                    }
                                }
                                Row(horizontalArrangement = Arrangement.Center){
                                    showImages(imagesUri = imagesList, contex)
                                }
                            }
                        }
                    }

                } else {
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(backGround)
                    ) {
                        Column(
                            modifier = modifier
                                .width(configuration.screenWidthDp.dp / 2)
                                .padding(horizontal = hig),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        WebView(context).apply {
                                            // Imposta le opzioni WebView necessarie
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true
                                        }
                                    },
                                    update = { webView ->
                                        // Carica la mappa Leaflet nel WebView
                                        webView.loadUrl("file:///android_asset/leaflet/index.html")
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier.width(configuration.screenWidthDp.dp / 2)
                        ) {
                            items(1) { element ->
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxSize(),
                                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                )
                                {
                                    Row(modifier = modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(10.dp)
                                    )
                                    {
                                        Column() {
                                            Text(
                                                text = "Utente:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Data:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Numero Esemplari:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Posizione:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Animale:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Specie:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Mare:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Vento:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Note:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = utente,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = data,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = numeroEsemplari,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = posizione,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = selectedOptionText,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row() {
                                                Text(
                                                    text = selectedOptionTextSpecie,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Button(
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .padding(0.dp)
                                                        .align(Alignment.CenterVertically),
                                                    colors = ButtonDefaults.buttonColors(
                                                        MaterialTheme.colorScheme.secondaryContainer,
                                                        MaterialTheme.colorScheme.primary
                                                    ),
                                                    contentPadding = PaddingValues(0.dp),
                                                    onClick = { showFilterInfoSpecie = true }) {
                                                    Icon(
                                                        modifier = Modifier.fillMaxSize(),
                                                        imageVector = Icons.Filled.Info,
                                                        contentDescription = "Vedi dettagli specie"
                                                    )
                                                }
                                                InfoSpecie(showFilterInfoSpecie =  showFilterInfoSpecie){
                                                    showFilterInfoSpecie = false
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = mare,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = vento,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = note,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else -> {                                                           /** SightingViewScreen verticale */
            Column(modifier= Modifier
                .background(backGround)
                .fillMaxSize()) {
                if(owner){
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(backGround)
                    ) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = min / 2),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            items(1) { element ->
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AndroidView(
                                        factory = { context ->
                                            WebView(context).apply {
                                                // Imposta le opzioni WebView necessarie
                                                settings.javaScriptEnabled = true
                                                settings.domStorageEnabled = true
                                            }
                                        },
                                        update = { webView ->
                                            // Carica la mappa Leaflet nel WebView
                                            webView.loadUrl("file:///android_asset/leaflet/index.html")
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Row() {
                                    Column() {
                                        Text(
                                            text = "Utente:",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Data:",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = utente,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = data,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                                // Numero Esemplari
                                OutlinedTextField(
                                    value = numeroEsemplari,
                                    onValueChange = { numeroEsemplari = it },
                                    label = { Text("Numero Esemplari") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(
                                ) {
                                    OutlinedTextField(
                                        value = posizione,
                                        onValueChange = { posizione = it },
                                        label = { Text("Posizione") },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Decimal
                                        ),
                                        singleLine = true,
                                        trailingIcon = {
                                            IconButton(onClick = { }) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_gps_fixed_24),
                                                    contentDescription = "GPS",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                // Mare
                                OutlinedTextField(
                                    value = mare,
                                    onValueChange = { mare = it },
                                    label = { Text("Mare") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                                // Vento
                                OutlinedTextField(
                                    value = vento,
                                    onValueChange = { vento = it },
                                    label = { Text("Vento") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primaryContainer)
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
                                                    selectedOptionText =
                                                        selectionOption
                                                    expanded = false
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                // Specie
                                Row() {
                                    ExposedDropdownMenuBox(
                                        modifier = Modifier
                                            .width(245.dp)
                                            .background(MaterialTheme.colorScheme.primaryContainer)
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.outline,
                                                RoundedCornerShape(2.dp)
                                            ),
                                        expanded = expandedSpecie,
                                        onExpandedChange = {
                                            expandedSpecie = !expandedSpecie
                                        },
                                    ) {
                                        TextField(
                                            // The `menuAnchor` modifier must be passed to the text field for correctness.
                                            modifier = Modifier.menuAnchor(),
                                            readOnly = true,
                                            value = selectedOptionTextSpecie,
                                            onValueChange = {},
                                            label = { Text("Specie") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(
                                                    expanded = expandedSpecie
                                                )
                                            },
                                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedSpecie,
                                            onDismissRequest = { expanded = false },
                                        ) {
                                            optionsSpecie.forEach { selectionOptionSpecie ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            selectionOptionSpecie
                                                        )
                                                    },
                                                    onClick = {
                                                        selectedOptionTextSpecie =
                                                            selectionOptionSpecie
                                                        expandedSpecie = false
                                                    },
                                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                )
                                            }
                                        }
                                    }
                                    Button(
                                        modifier = Modifier
                                            .size(35.dp)
                                            .padding(0.dp)
                                            .align(Alignment.CenterVertically),
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.primary
                                        ),
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = { showFilterInfoSpecie = true }) {
                                        Icon(
                                            modifier = Modifier.fillMaxSize(),
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = "Vedi dettagli specie"
                                        )
                                    }
                                    InfoSpecie(showFilterInfoSpecie =  showFilterInfoSpecie){
                                        showFilterInfoSpecie = false
                                    }
                                }
                                // Note
                                OutlinedTextField(
                                    value = note,
                                    onValueChange = { note = it },
                                    label = { Text("Note") }
                                )

                                Row() {
                                    // Submit button
                                    Button(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp),
                                        onClick = {
                                            (contex as MainActivity).requestCameraPermission(cu.toString(), count)
                                            count+=1
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.primary
                                        ),

                                        ) {
                                        Icon(
                                            painterResource(id = R.drawable.baseline_camera_alt_24),
                                            contentDescription = "Foto"
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(text = "AGGIUNGI")
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Button(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp),
                                        onClick = {
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.primary
                                        ),

                                        ) {
                                        Icon(
                                            painterResource(id = R.drawable.baseline_save_24),
                                            contentDescription = "Salva"
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(text = "SALVA")
                                    }
                                }
                                showImages(imagesUri = imagesList, contex)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(backGround)
                    ) {
                        LazyColumn(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = min / 2),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(1) { element ->
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AndroidView(
                                        factory = { context ->
                                            WebView(context).apply {
                                                // Imposta le opzioni WebView necessarie
                                                settings.javaScriptEnabled = true
                                                settings.domStorageEnabled = true
                                            }
                                        },
                                        update = { webView ->
                                            // Carica la mappa Leaflet nel WebView
                                            webView.loadUrl("file:///android_asset/leaflet/index.html")
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(),
                                    border= BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    colors = CardDefaults.outlinedCardColors(),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ){
                                    Row(modifier= Modifier
                                        .padding(0.dp)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .fillMaxSize()) {
                                        Column(modifier = Modifier
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(5.dp)) {
                                            Text(
                                                text = "Utente:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Data:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Numero Esemplari:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Posizione:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Animale:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Specie:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Mare:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Vento:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Note:",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                                .padding(5.dp)
                                        ) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = utente,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = data,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = numeroEsemplari,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = posizione,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = selectedOptionText,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row {
                                                Text(
                                                    text = selectedOptionTextSpecie,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Button(
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .padding(0.dp)
                                                        .align(Alignment.CenterVertically),
                                                    colors = ButtonDefaults.buttonColors(
                                                        MaterialTheme.colorScheme.secondaryContainer,
                                                        MaterialTheme.colorScheme.primary
                                                    ),
                                                    contentPadding = PaddingValues(0.dp),
                                                    onClick = { showFilterInfoSpecie = true }) {
                                                    Icon(
                                                        modifier = Modifier.fillMaxSize(),
                                                        imageVector = Icons.Filled.Info,
                                                        contentDescription = "Vedi dettagli specie"
                                                    )
                                                }
                                                InfoSpecie(showFilterInfoSpecie =  showFilterInfoSpecie){
                                                    showFilterInfoSpecie = false
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = mare,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = vento,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = note,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoSpecie (showFilterInfoSpecie: Boolean, onCloseDialog: () -> Unit){
    if (showFilterInfoSpecie){
        AlertDialog(
            onDismissRequest = onCloseDialog,
            title = { Text("Dettagli") },
            text = {
                Column {
                    Row{
                        LazyColumn(
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            items(1) { element ->
                                Text(
                                    text = "Nomenclatura binomiale:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Descrizione...",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Descrizione:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Descrizione...",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Dimensione:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Descrizione...",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Curiosità:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Descrizione...",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onCloseDialog) {
                            Text("Chiudi")
                        }
                    }
                }
            },
            confirmButton = {/** TODO */}
        )
    }
}

fun getImage(){

}

