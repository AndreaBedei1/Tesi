package com.example.seawatch

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingScreen(
    avvistamentiViewModel: AvvistamentiViewModel,
    goToHome:()->Unit,
    modifier: Modifier = Modifier,
    ) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var utente by rememberSaveable { mutableStateOf(em) }
    val currentDateTimeClock = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.ITALIAN)
    val formattedDateTime = currentDateTimeClock.format(formatter)
    var data by rememberSaveable { mutableStateOf(formattedDateTime) }
    var numeroEsemplari by rememberSaveable { mutableStateOf("") }
    var posizione by rememberSaveable { mutableStateOf("") }
    var mare by rememberSaveable { mutableStateOf("") }
    var vento by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    val options by rememberSaveable { mutableStateOf(animaList) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var optionsSpecie by rememberSaveable { mutableStateOf(mutableListOf<String>()) }
    var expandedSpecie by rememberSaveable { mutableStateOf(false) }
    var selectedOptionTextSpecie by rememberSaveable { mutableStateOf("") }
    var showFilterInfoSpecie by rememberSaveable { mutableStateOf(false) }
    val contex = LocalContext.current
    val currentDateTime by rememberSaveable {mutableStateOf( System.currentTimeMillis().toString())}
    var count by rememberSaveable {mutableStateOf(0)}
    var imagesList =(contex as MainActivity).getAllSavedImages(currentDateTime.toString())
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    shape= RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { var immagini=""
                        for (elemento in imagesList){
                            immagini=immagini+elemento+";"
                        }
                        val a=AvvistamentiDaCaricare(System.currentTimeMillis().toString(), utente, data, numeroEsemplari, posizione, selectedOptionText, selectedOptionTextSpecie, mare, vento, note, immagini)
                        avvistamentiViewModel.insert(a)
                        showConfirmDialog=true },
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                ) {
                    Icon(imageVector = Icons.Filled.Check, "Conferma aggiunta avvistamento")
                }
            }, floatingActionButtonPosition = FabPosition.End){paddingValues->
                Column(modifier= Modifier
                    .background(backGround)
                    .padding(paddingValues)) {
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
                                                    },
                                                    readOnly = true
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
                                                                val client = OkHttpClient()
                                                                val formBody = MultipartBody.Builder()
                                                                    .setType(MultipartBody.FORM)
                                                                    .addFormDataPart("selector", selectionOption)
                                                                    .addFormDataPart("request", "slcSottospecie")
                                                                    .build()
                                                                val request = Request.Builder()
                                                                    .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/single_sighting/single_api.php")
                                                                    .post(formBody)
                                                                    .build()

                                                                client.newCall(request).enqueue(object :
                                                                    Callback {
                                                                    override fun onFailure(call: Call, e: IOException) {

                                                                    }

                                                                    override fun onResponse(call: Call, response: Response) {
                                                                        val body = response.body?.string()
                                                                        val l = body.toString()
                                                                        val list = JSONArray(l)
                                                                        for (i in 0..list.length() - 1 step 1) {
                                                                            specieList.add((list.get(i) as JSONObject).get("descr_select").toString())
                                                                        }
                                                                        optionsSpecie=specieList
                                                                    }
                                                                })
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
                                    if (showConfirmDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showConfirmDialog = false; goToHome() },
                                            title = { Text("AVVISO") },
                                            text = {
                                                Column {
                                                    Row(){
                                                        Text(text="Avvistamento caricato localmente in maniera corretta! Per caricarlo online si prega di premere l'apposito pulsante.")
                                                    }
                                                    Row(
                                                        modifier = Modifier
                                                            .padding(8.dp)
                                                            .fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.End
                                                    ) {
                                                        TextButton(onClick = { showConfirmDialog = false; goToHome() }) {
                                                            Text("CHIUDI")
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
                    }
                }
            }
        }
        else -> {
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    shape= RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { var immagini=""
                        for (elemento in imagesList){
                            immagini=immagini+elemento+";"
                        }
                        val a=AvvistamentiDaCaricare(System.currentTimeMillis().toString(), utente, data, numeroEsemplari, posizione, selectedOptionText, selectedOptionTextSpecie, mare, vento, note, immagini)
                        avvistamentiViewModel.insert(a)
                        showConfirmDialog=true },
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                ) {
                    Icon(imageVector = Icons.Filled.Check, "Conferma aggiunta avvistamento")
                }
            }, floatingActionButtonPosition = FabPosition.End){paddingValues->
                LazyColumn(modifier= Modifier
                    .background(backGround)
                    .padding(paddingValues)
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
                                        },
                                        readOnly = true
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
                                if (showConfirmDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showConfirmDialog = false; goToHome() },
                                        title = { Text("AVVISO") },
                                        text = {
                                            Column {
                                                Row(){
                                                    Text(text="Avvistamento caricato localmente in maniera corretta! Per caricarlo online si prega di accedere e di premere l'apposito pulsante.")
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(8.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.End
                                                ) {
                                                    TextButton(onClick = { showConfirmDialog = false; goToHome() }) {
                                                        Text("CHIUDI")
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
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingScreenOffline(
    avvistamentiViewModel: AvvistamentiViewModel,
    goToLogin:()->Unit,
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
    val options = listOf("Sconosciuto", "Altro", "Balena", "Delfino", "Foca", "Razza", "Squalo", "Tartaruga", "Tonno")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    val optionsSpecie = listOf("Specie 1", "Specie 2", "Specie 3", "Specie 4", "Specie 5")
    var expandedSpecie by rememberSaveable { mutableStateOf(false) }
    var selectedOptionTextSpecie by rememberSaveable { mutableStateOf("") }
    val contex = LocalContext.current
    val cu by rememberSaveable { mutableStateOf( System.currentTimeMillis().toString()) }
    var count by rememberSaveable { mutableStateOf(0) }
    var imagesList =(contex as MainActivity).getAllSavedImages(cu.toString())
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** Login orizzontale */
            Scaffold(floatingActionButton={
                /** navController.navigate(NavigationScreen.ProfileSettings.name)*/
                /** navController.navigate(NavigationScreen.ProfileSettings.name)*/
                FloatingActionButton(
                    shape= RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { var immagini=""
                        for (elemento in imagesList){
                            immagini=immagini+elemento+";"
                        }
                        val a=AvvistamentiDaCaricare(System.currentTimeMillis().toString(), "Offline", data, numeroEsemplari, posizione, selectedOptionText, selectedOptionTextSpecie, mare, vento, note, immagini)
                        avvistamentiViewModel.insert(a)
                        showConfirmDialog=true },
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(imageVector = Icons.Filled.Check, "Aggiungi avvistamento")
                }
            }, floatingActionButtonPosition = FabPosition.End){paddingValues ->
                Column(modifier= Modifier
                    .background(backGround)
                    .padding(paddingValues)) {
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
                                    if (showConfirmDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showConfirmDialog = false; goToLogin() },
                                            title = { Text("AVVISO") },
                                            text = {
                                                Text(text="Avvistamento caricato localmente in maniera corretta! Per caricarlo online si prega di accedere e di premere l'apposito pulsante.")
                                            },
                                            confirmButton = {
                                                TextButton(onClick = { showConfirmDialog = false; goToLogin() }) {
                                                    Text("CHIUDI")
                                                }
                                            }
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
            /** navController.navigate(NavigationScreen.ProfileSettings.name)*/
            Scaffold(floatingActionButton={
                /** navController.navigate(NavigationScreen.ProfileSettings.name)*/
                /** navController.navigate(NavigationScreen.ProfileSettings.name)*/
                FloatingActionButton(
                    shape= RoundedCornerShape(50.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        var immagini=""
                        for (elemento in imagesList){
                            immagini=immagini+elemento+";"
                        }
                        val a=AvvistamentiDaCaricare(System.currentTimeMillis().toString(), "Offline", data, numeroEsemplari, posizione, selectedOptionText, selectedOptionTextSpecie, mare, vento, note, immagini)
                        avvistamentiViewModel.insert(a)
                        showConfirmDialog=true },
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(imageVector = Icons.Filled.Check, "Aggiungi avvistamento")
                }
            }, floatingActionButtonPosition = FabPosition.End){ innerPadding->
                LazyColumn(modifier= Modifier
                    .padding(innerPadding)
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
                                    Spacer(modifier = Modifier.width(3.dp))
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
                                                text = { Text(selectionOptionSpecie) },
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
                                if (showConfirmDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showConfirmDialog = false; goToLogin() },
                                        title = { Text("AVVISO") },
                                        text = {
                                            Column {
                                                Row(){
                                                    Text(text="Avvistamento caricato localmente in maniera corretta! Per caricarlo online si prega di accedere e di premere l'apposito pulsante.")
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .padding(8.dp)
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.End
                                                ) {
                                                    TextButton(onClick = { showConfirmDialog = false; goToLogin() }) {
                                                        Text("CHIUDI")
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
                }
            }
        }
    }
}
