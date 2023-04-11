package com.example.seawatch

import android.content.res.Configuration
import android.webkit.WebView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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