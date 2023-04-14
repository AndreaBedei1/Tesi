package com.example.seawatch

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.seawatch.data.User
import com.example.seawatch.data.UserViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

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
    Column(
        Modifier
            .selectableGroup()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()) {
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
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { errorMessage = "" },
            title = { Text(text = "Aggiornamento") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = { errorMessage = "" }) {
                    Text(text = "OK")
                }
            }
        )
    }

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
                onClick = {
                    if(newPassword==confirmPassword){
                        errorMessage="Attendi..."
                        val client = OkHttpClient()
                        val formBody = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user", em)
                            .addFormDataPart("request", "getKeyMob")
                            .build()
                        val request = Request.Builder()
                            .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/main_settings/settings_api.php")
                            .post(formBody)
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                errorMessage = "Impossibile comunicare col server, controllare la connessione."
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val body = response.body?.string()
                                val msg = body.toString()
                                val jsn = JSONObject(msg)

                                val oldP = calculateHmacSha512(oldPassword, jsn.get("key").toString())
                                val nPwd = calculateHmacSha512(newPassword, jsn.get("key").toString())
                                // Dopo aver preso la chiave di criptaggio salvo la nuova password
                                val client = OkHttpClient()
                                val formBody = MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("old", oldP)
                                    .addFormDataPart("new", nPwd)
                                    .addFormDataPart("user", em)
                                    .addFormDataPart("request", "changePwdMob")
                                    .build()
                                val request = Request.Builder()
                                    .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/main_settings/settings_api.php")
                                    .post(formBody)
                                    .build()

                                client.newCall(request).enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        errorMessage = "Impossibile comunicare col server, controllare la connessione."
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        val body = response.body?.string()
                                        val msg = body.toString()
                                        if(JSONObject(msg).get("stato").toString()=="true"){
                                            errorMessage="Modifica avvenuta con successo."
                                            oldPassword=""
                                            newPassword=""
                                            confirmPassword=""
                                        } else {
                                            errorMessage = "La vecchia password non è corretta!"
                                        }
                                    }
                                })
                            }
                        })
                    } else {
                        errorMessage = "Le ultime due password non corrispondono."
                    }
                },
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
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var nome by rememberSaveable { mutableStateOf("") }
    var cognome by rememberSaveable { mutableStateOf("") }
    var profilo by rememberSaveable { mutableStateOf("R.drawable.sea") }
    val context = LocalContext.current
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var currentDateTime by rememberSaveable {mutableStateOf( System.currentTimeMillis().toString())}
    var imagesList =(context as MainActivity).getAllSavedImages(currentDateTime)


    if(imagesList.isNotEmpty()){
        val file = File(context.cacheDir, "image.jpg")
        val outputStream = FileOutputStream(file)
        lastImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Log.e("KEYYY", lastImageBitmap?.allocationByteCount.toString())
        // restituisci l'URI del file temporaneo
        if (file == null) {
            errorMessage = "Errore nel caricamento locale del file"
        } else {
            val requestUrl = "https://isi-seawatch.csr.unibo.it/Sito/sito/templates/single_sighting/single_api.php" // Sostituisci con l'URL del server
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", em)
                .addFormDataPart(
                    "file",
                    file.name,
                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                .addFormDataPart("request", "addImageProfileMob")
                .build()

            val request = Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    errorMessage = e.toString()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    Log.e("KEYYYCATTIVA", body.toString())
                }
            })
        }

        currentDateTime = System.currentTimeMillis().toString()
        imagesList =(context as MainActivity).getAllSavedImages(currentDateTime)
    }

    val userItems: List<User> by userViewModel.all.collectAsState(initial = listOf())
    for(elem in userItems){
        if(elem.mail== em){
            nome = elem.nome
            cognome = elem.cognome
            break
        }
    }

    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { errorMessage = "" },
            title = { Text(text = "Attenzione") },
            text = { Text(text = errorMessage) },
            confirmButton = {
                Button(onClick = { errorMessage = "" }) {
                    Text(text = "OK")
                }
            }
        )
    }

    if(isNetworkAvailable(context)) {
        val client = OkHttpClient()
        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user", em)
            .addFormDataPart("request", "getUserInfoMob")
            .build()
        val request = Request.Builder()
            .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/main_settings/settings_api.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val msg = JSONArray(body.toString())
                profilo = try {
                    "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (msg.get(0) as JSONObject).get(
                        "Img"
                    ).toString()
                } catch (e: Exception) {
                    "R.drawable.sea"
                }
            }
        })
    }

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
                            painter = rememberImagePainter(
                                data = profilo,
                            ),
                            contentDescription = "Immagine del profilo",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
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
                            colors = TextFieldDefaults.outlinedTextFieldColors(),
                            enabled = isNetworkAvailable(context)
                        )
                        Spacer(modifier = Modifier.height(min))
                        TextField(
                            value = cognome,
                            onValueChange = { cognome = it },
                            label = { Text("Cognome") },
                            singleLine = true,
                            placeholder = { Text("Rossi") },
                            modifier = Modifier.background(backGround),
                            colors = TextFieldDefaults.outlinedTextFieldColors(),
                            enabled = isNetworkAvailable(context)
                        )
                        Spacer(modifier = Modifier.height(hig+15.dp))
                        Button(
                            onClick = {
                                if (isNetworkAvailable(context)){
                                    val client = OkHttpClient()
                                    val formBody = MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("user", em)
                                        .addFormDataPart("nome", nome)
                                        .addFormDataPart("cognome", cognome)
                                        .addFormDataPart("request", "setUserInfoMob")
                                        .build()
                                    val request = Request.Builder()
                                        .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/main_settings/settings_api.php")
                                        .post(formBody)
                                        .build()

                                    client.newCall(request).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            errorMessage = "Impossibile comunicare col server."
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            val body = response.body?.string()
                                            val msg = JSONObject(body.toString())
                                            if(msg.get("stato")==true){
                                                errorMessage = "Cambiamento dati avvenuto con successo."
                                            } else {
                                                errorMessage = "Cambiamento dati non avvenuto."
                                            }
                                        }
                                    })
                                } else {
                                    errorMessage = "Nessuna connessione riprovare quando si è collegati."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            modifier = modifier.widthIn(min = 200.dp)
                        ) {
                            Text("AGGIORNA")
                        }
                    }
                }
            }
        }
        else -> {
            /** profile verticale */
            //showImages(imagesUri = imagesList, context = context )
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(hig))
                    Image(
                        painter = rememberImagePainter(
                            data = profilo,
                        ),
                        contentDescription = "Immagine del profilo",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(min))
                    Button(
                        onClick = {
                             if( isNetworkAvailable(context)){
                                 (context as MainActivity).requestCameraPermission(currentDateTime.toString())
                             } else {
                                 errorMessage="Nessuna connessione di rete"
                             }
                        },
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
                        colors = TextFieldDefaults.outlinedTextFieldColors(),
                        enabled = isNetworkAvailable(context)
                    )
                    Spacer(modifier = Modifier.height(min))
                    TextField(
                        value = cognome,
                        onValueChange = { cognome = it },
                        label = { Text("Cognome") },
                        singleLine = true,
                        placeholder = { Text("Rossi") },
                        modifier = Modifier.background(backGround),
                        colors = TextFieldDefaults.outlinedTextFieldColors(),
                        enabled = isNetworkAvailable(context)
                    )
                    Spacer(modifier = Modifier.height(med))
                    Button(
                        onClick = {
                            if (isNetworkAvailable(context)){
                                val client = OkHttpClient()
                                val formBody = MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("user", em)
                                    .addFormDataPart("nome", nome)
                                    .addFormDataPart("cognome", cognome)
                                    .addFormDataPart("request", "setUserInfoMob")
                                    .build()
                                val request = Request.Builder()
                                    .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/main_settings/settings_api.php")
                                    .post(formBody)
                                    .build()

                                client.newCall(request).enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        errorMessage = "Impossibile comunicare col server."
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        val body = response.body?.string()
                                        val msg = JSONObject(body.toString())
                                        if(msg.get("stato")==true){
                                            errorMessage = "Cambiamento dati avvenuto con successo."
                                        } else {
                                            errorMessage = "Cambiamento dati non avvenuto."
                                        }
                                    }
                                })
                            } else {
                                errorMessage = "Nessuna connessione riprovare quando si è collegati."
                            }
                        },
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

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    if (network != null) {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
    return false
}
