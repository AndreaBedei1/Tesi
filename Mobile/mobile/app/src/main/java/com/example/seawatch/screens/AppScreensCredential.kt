package com.example.seawatch

import android.content.SharedPreferences
import android.content.res.Configuration
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

var ok = true
var em=""
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    goToHome: () -> Unit,
    goToSignUp:() ->Unit,
    modifier: Modifier = Modifier,
    sharedPrefForLogin: SharedPreferences,
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
                                /** TODO Prima controlla se l'utente esiste nel DB online con if */

                                /** TODO Prima controlla se l'utente esiste nel DB online con if */
                                /** TODO Prima controlla se l'utente esiste nel DB online con if */
                                /** TODO Prima controlla se l'utente esiste nel DB online con if */
                                /** TODO Prima controlla se l'utente esiste nel DB online con if */
                                /** TODO Prima controlla se l'utente esiste nel DB online con if */

                                /** TODO Prima controlla se l'utente esiste nel DB online con if */
                                with(sharedPrefForLogin.edit()){
                                    putString("USER", mail)
                                    apply()
                                }
                                ok = false
                                em=mail
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
                            em=mail
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