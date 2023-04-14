package com.example.seawatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.FragmentActivity
import coil.compose.rememberImagePainter
import com.example.seawatch.data.*
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*



var entratoRete = false
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    userViewModel: UserViewModel
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp/40
    val med = configuration.screenHeightDp.dp/20
    val hig = configuration.screenHeightDp.dp/10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    var nome by rememberSaveable { mutableStateOf("") }
    var cognome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var profilo by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var errorMessage by rememberSaveable { mutableStateOf("") }

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


    if (isNetworkAvailable(context)) {
        val client = OkHttpClient()
        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user", profileViewModel.mail)
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
                try {
                    nome = (msg.get(0) as JSONObject).get("Nome").toString()
                    cognome = (msg.get(0) as JSONObject).get("Cognome").toString()
                    email = profileViewModel.mail
                } catch (e: Exception) {
                }
            }
        })
    } else {
        val userItems: List<User> by userViewModel.all.collectAsState(initial = listOf())
        var b = true
        email = em
        for(elem in userItems){
            if(elem.mail == em){
                nome = elem.nome
                cognome = elem.cognome
                b=false
                break
            }
        }
        if (b) {
            if(!entratoRete) {
                errorMessage =
                    "Errore di connessione impossibile prendere i dati dell'utente richiesto."
            }
            entratoRete = true
        }
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {                   /** Profilo orizzontale */
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
                        val scale = if(profilo=="https://isi-seawatch.csr.unibo.it/Sito/img/profilo/profilo.jpg" || !isNetworkAvailable(context)){1.0f}else{1.8f}
                        Image(
                            painter = rememberImagePainter(
                                data = if(isNetworkAvailable(context)){profilo} else {R.drawable.profilo},
                            ),
                            contentDescription = "Immagine del profilo",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .scale(scale)
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
                                Text(text=nome, style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(min+6.dp))
                                Text(text=cognome, style = MaterialTheme.typography.bodyLarge)
                                Spacer(modifier = Modifier.height(min+6.dp))
                                Text(text=email, textDecoration = TextDecoration.Underline, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
        else -> {                                                   /** Profilo verticale */
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(backGround),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) { element ->
                    Spacer(modifier = Modifier.height(hig))
                    val scale = if(profilo=="https://isi-seawatch.csr.unibo.it/Sito/img/profilo/profilo.jpg" || !isNetworkAvailable(context)){1.0f}else{1.8f}
                    Image(
                        painter = rememberImagePainter(
                            data = if(isNetworkAvailable(context)){profilo} else {R.drawable.profilo} ,
                        ),
                        contentDescription = "Immagine del profilo",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .scale(scale)
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
                            Text(text=nome, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(min+6.dp))
                            Text(text=cognome, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(min+6.dp))
                            Text(text=email, textDecoration = TextDecoration.Underline, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

data class MarkerData(
    val latitude: String,
    val longitude: String,
    val data: String,
    val animale: String,
    val specie: String
)

var mapset = false

var fav = true
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToSighting: () -> Unit,
    goToProfile: () -> Unit,
    barHeight: Int,
    modifier: Modifier = Modifier,
    favouriteViewModel: FavouriteViewModel,
    listItems:List<Favourite>,
    profileViewModel: ProfileViewModel
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp / 40
    val med = configuration.screenHeightDp.dp / 20
    val hig = configuration.screenHeightDp.dp / 10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    val context = LocalContext.current
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var avvList by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var isFilter by rememberSaveable { mutableStateOf(false) }
    var isAfter by rememberSaveable { mutableStateOf(true) }
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }
    val options by rememberSaveable { mutableStateOf(getAnimal()) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var filterPref by rememberSaveable { mutableStateOf(false) }
    var filterAnima by rememberSaveable { mutableStateOf("") }
    var listFavourite by rememberSaveable {mutableStateOf(mutableListOf<String>())}
    mapset = false

    if(fav){
        fav=false
        for (el in listItems) {
            if (el.utente == em) {
                listFavourite.add(el.avvistamento)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Home")
                    }
                },
                modifier = Modifier.height(barHeight.dp),
                actions = {
                    var favoriteFilter = filterPref
                    var tmp by rememberSaveable { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(0.dp),
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
                                            checked =  tmp,
                                            onCheckedChange = {favoriteFilter = it; tmp = !tmp },
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
                                }
                            },
                            confirmButton = {
                                Row(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { showFilterDialog = false }) {
                                        Text("Annulla")
                                    }
                                    TextButton(onClick = { showFilterDialog = false; filterPref=favoriteFilter; filterAnima=selectedOptionText; isFilter=true }) {
                                        Text("Applica")

                                    }
                                }
                            }
                        )
                    }
                }
            )
        }
    ){ it ->
        LaunchedEffect(key1 = isLoading, key2 = isFilter) {
            if (isLoading) {
                val client = OkHttpClient()
                val formBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("request", "tbl_avvistamenti")
                    .build()
                val request = Request.Builder()
                    .url("https://isi-seawatch.csr.unibo.it/Sito/sito/templates/main_sighting/sighting_api.php")
                    .post(formBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        errorMessage = "Impossibile comunicare col server."
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        avvList = body.toString()
                        isLoading = false
                    }
                })
            }
            if(isFilter){
                isFilter=false
                isAfter=true
            }
        }

        if (isLoading) {
            AlertDialog(
                onDismissRequest = { isLoading = false },
                title = { Text(text = "Caricamento...") },
                text = { Text(text = "Attendi che i dati vengano caricati.") },
                confirmButton = {}
            )
        } else {

            var list = JSONArray()
            var temp = JSONArray(avvList)

            for (i in 0 until temp.length() step 1) {
                if (filterAnima == "" && !filterPref) {
                    list.put(temp.get(i))
                } else if (filterAnima != "" && !filterPref) {
                    if((temp.get(i) as JSONObject).get("Anima_Nome").toString()==filterAnima){
                        list.put(temp.get(i))
                    }
                } else if(filterAnima == "" && filterPref) {
                    if((temp.get(i) as JSONObject).get("ID").toString() in listFavourite){
                        list.put(temp.get(i))
                    }
                } else {
                    if((temp.get(i) as JSONObject).get("Anima_Nome").toString()==filterAnima && (temp.get(i) as JSONObject).get("ID").toString() in listFavourite){
                        list.put(temp.get(i))
                    }
                }
            }

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
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it),
                                contentAlignment = Alignment.Center
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        WebView(context).apply {
                                            // Imposta le opzioni WebView necessarie
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true

                                            webViewClient = object : WebViewClient() {
                                                override fun onPageFinished(view: WebView?, url: String?) {
                                                    super.onPageFinished(view, url)
                                                    var mkList = mutableListOf<MarkerData>()
                                                    for (i in 0..list.length() - 1 step 1) {
                                                        mkList.add(
                                                            MarkerData(
                                                                (list.get(i) as JSONObject).get("Latid").toString(),
                                                                (list.get(i) as JSONObject).get("Long").toString(),
                                                                (list.get(i) as JSONObject).get("Data").toString(),
                                                                (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                                                (list.get(i) as JSONObject).get("Specie_Nome").toString()
                                                            )
                                                        )
                                                    }
                                                    val gson = Gson()
                                                    var markerDataJson = gson.toJson(mkList)
                                                    var currentMarkerDataJson = markerDataJson
                                                    try {
                                                        view?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                                    } catch (e: Exception) {
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    update = { webView ->
                                        if(isAfter || true) {
                                            if (!mapset) {
                                                webView.loadUrl("file:///android_asset/leaflet/index.html")
                                                mapset = true
                                            } else {
                                                webView?.evaluateJavascript("removeMarkers()", null)
                                                var mkList = mutableListOf<MarkerData>()
                                                for (i in 0..list.length() - 1 step 1) {
                                                    mkList.add(
                                                        MarkerData(
                                                            (list.get(i) as JSONObject).get("Latid").toString(),
                                                            (list.get(i) as JSONObject).get("Long").toString(),
                                                            (list.get(i) as JSONObject).get("Data").toString(),
                                                            (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                                            (list.get(i) as JSONObject).get("Specie_Nome").toString()
                                                        )
                                                    )
                                                }
                                                val gson = Gson()
                                                var markerDataJson = gson.toJson(mkList)
                                                var currentMarkerDataJson = markerDataJson
                                                try {
                                                    webView?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                                } catch (e: Exception) {
                                                }
                                            }
                                            isAfter=false
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                            items(1){element->
                                Spacer(modifier = Modifier.height(40.dp))
                                Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                                    Column() {
                                        for (i in 0..list.length()-1 step 2) {
                                            Card(
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .size(width = 180.dp, height = 160.dp),
                                                border = BorderStroke(2.dp, Color.Black),
                                                colors = CardDefaults.outlinedCardColors(),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                onClick = { goToSighting() }
                                            ) {

                                                var isFavorite =(list.get(i) as JSONObject).get("ID").toString() in listFavourite
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(48.dp)
                                                                .clip(CircleShape)
                                                                .clickable { profileViewModel.set((list.get(i) as JSONObject).get("Email").toString()); goToProfile() }
                                                        ) {
                                                            val scale = if((list.get(i) as JSONObject).get("Img").toString()=="profilo.jpg"){1.0f}else{1.8f}
                                                            Image(
                                                                painter = rememberImagePainter(
                                                                    data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (list.get(
                                                                        i
                                                                    ) as JSONObject).get("Img")
                                                                        .toString(),
                                                                ),
                                                                contentDescription = "Immagine del profilo",
                                                                modifier = Modifier
                                                                    .size(48.dp)
                                                                    .clip(CircleShape)
                                                                    .scale(scale)
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(med + 30.dp))
                                                        IconButton(
                                                            onClick = {
                                                                if(isFavorite){
                                                                    favouriteViewModel.deletePref((list.get(i) as JSONObject).get("ID").toString(), em)
                                                                    listFavourite.remove((list.get(i) as JSONObject).get("ID").toString())
                                                                } else {
                                                                    favouriteViewModel.insert(Favourite(System.currentTimeMillis().toString(), em, (list.get(i) as JSONObject).get("ID").toString()))
                                                                    listFavourite.add((list.get(i) as JSONObject).get("ID").toString())
                                                                }
                                                                isFavorite = !isFavorite
                                                            },
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
                                                        text = (list.get(i) as JSONObject).get("Data")
                                                            .toString(),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = (list.get(i) as JSONObject).get("Anima_Nome")
                                                            .toString(),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = (list.get(i) as JSONObject).get("Nome")
                                                            .toString() + " " + (list.get(i) as JSONObject).get(
                                                            "Cognome"
                                                        ).toString(),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Column() {
                                        for (i in 1..list.length()-1 step 2) {
                                            Card(
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .size(width = 180.dp, height = 160.dp),
                                                border = BorderStroke(2.dp, Color.Black),
                                                colors = CardDefaults.outlinedCardColors(),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                onClick = { goToSighting() }
                                            ) {
                                                var isFavorite by rememberSaveable { mutableStateOf((list.get(i) as JSONObject).get("ID").toString() in listFavourite) }
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(12.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(48.dp)
                                                                .clip(CircleShape)
                                                                .clickable { profileViewModel.set((list.get(i) as JSONObject).get("Email").toString()); goToProfile() }
                                                        ) {
                                                            val scale = if((list.get(i) as JSONObject).get("Img").toString()=="profilo.jpg"){1.0f}else{1.8f}
                                                            Image(
                                                                painter = rememberImagePainter(
                                                                    data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (list.get(
                                                                        i
                                                                    ) as JSONObject).get("Img")
                                                                        .toString(),
                                                                ),
                                                                contentDescription = "Immagine del profilo",
                                                                modifier = Modifier
                                                                    .size(48.dp)
                                                                    .clip(CircleShape)
                                                                    .scale(scale)
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(med + 30.dp))
                                                        IconButton(
                                                            onClick = {
                                                                if(isFavorite){
                                                                    favouriteViewModel.deletePref((list.get(i) as JSONObject).get("ID").toString(), em)
                                                                    listFavourite.remove((list.get(i) as JSONObject).get("ID").toString())
                                                                } else {
                                                                    favouriteViewModel.insert(Favourite(System.currentTimeMillis().toString(), em, (list.get(i) as JSONObject).get("ID").toString()))
                                                                    listFavourite.add((list.get(i) as JSONObject).get("ID").toString())
                                                                }
                                                                isFavorite = !isFavorite
                                                            },
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
                                                        text = (list.get(i) as JSONObject).get("Data")
                                                            .toString(),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = (list.get(i) as JSONObject).get("Anima_Nome")
                                                            .toString(),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = (list.get(i) as JSONObject).get("Nome")
                                                            .toString() + " " + (list.get(i) as JSONObject).get(
                                                            "Cognome"
                                                        ).toString(),
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
                else -> {
                    /** Homepage verticale*/
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .background(backGround),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(1) { element ->
                            Spacer(modifier = Modifier.height(50.dp))
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AndroidView(
                                    factory = { context ->
                                        WebView(context).apply {
                                            // Imposta le opzioni WebView necessarie
                                            settings.javaScriptEnabled = true
                                            settings.domStorageEnabled = true

                                            webViewClient = object : WebViewClient() {
                                                override fun onPageFinished(view: WebView?, url: String?) {
                                                    super.onPageFinished(view, url)
                                                    var mkList = mutableListOf<MarkerData>()
                                                    for (i in 0..list.length() - 1 step 1) {
                                                        mkList.add(
                                                            MarkerData(
                                                                (list.get(i) as JSONObject).get("Latid").toString(),
                                                                (list.get(i) as JSONObject).get("Long").toString(),
                                                                (list.get(i) as JSONObject).get("Data").toString(),
                                                                (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                                                (list.get(i) as JSONObject).get("Specie_Nome").toString()
                                                            )
                                                        )
                                                    }
                                                    val gson = Gson()
                                                    var markerDataJson = gson.toJson(mkList)
                                                    var currentMarkerDataJson = markerDataJson
                                                    try {
                                                        view?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                                    } catch (e: Exception) {
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    update = { webView ->
                                        if(isAfter || true) {
                                            if (!mapset) {
                                                webView.loadUrl("file:///android_asset/leaflet/index.html")
                                                mapset = true
                                            } else {
                                                webView?.evaluateJavascript("removeMarkers()", null)
                                                var mkList = mutableListOf<MarkerData>()
                                                for (i in 0..list.length() - 1 step 1) {
                                                    mkList.add(
                                                        MarkerData(
                                                            (list.get(i) as JSONObject).get("Latid").toString(),
                                                            (list.get(i) as JSONObject).get("Long").toString(),
                                                            (list.get(i) as JSONObject).get("Data").toString(),
                                                            (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                                            (list.get(i) as JSONObject).get("Specie_Nome").toString()
                                                        )
                                                    )
                                                }
                                                val gson = Gson()
                                                var markerDataJson = gson.toJson(mkList)
                                                var currentMarkerDataJson = markerDataJson
                                                try {
                                                    webView?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                                } catch (e: Exception) {
                                                }
                                            }
                                            isAfter=false
                                        }
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
                            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
                                Column(modifier=Modifier.width((configuration.screenWidthDp/2).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    for (i in 0..list.length()-1 step 2){
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
                                            var isFavorite =(list.get(i) as JSONObject).get("ID").toString() in listFavourite
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(48.dp)
                                                            .clip(CircleShape)
                                                            .clickable { profileViewModel.set((list.get(i) as JSONObject).get("Email").toString()); goToProfile() }
                                                    ) {
                                                        val scale = if((list.get(i) as JSONObject).get("Img").toString()=="profilo.jpg"){1.0f}else{1.8f}
                                                        Image(
                                                            painter = rememberImagePainter(
                                                                data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (list.get(
                                                                    i
                                                                ) as JSONObject).get("Img")
                                                                    .toString(),
                                                            ),
                                                            contentDescription = "Immagine del profilo",
                                                            modifier = Modifier
                                                                .size(48.dp)
                                                                .clip(CircleShape)
                                                                .scale(scale)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(med+30.dp))
                                                    IconButton(
                                                        onClick = {
                                                            if(isFavorite){
                                                                favouriteViewModel.deletePref((list.get(i) as JSONObject).get("ID").toString(), em)
                                                                listFavourite.remove((list.get(i) as JSONObject).get("ID").toString())
                                                            } else {
                                                                favouriteViewModel.insert(Favourite(System.currentTimeMillis().toString(), em, (list.get(i) as JSONObject).get("ID").toString()))
                                                                listFavourite.add((list.get(i) as JSONObject).get("ID").toString())
                                                            }
                                                            isFavorite = !isFavorite
                                                      },
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
                                                    text =  (list.get(i) as JSONObject).get("Data").toString(),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = (list.get(i) as JSONObject).get("Nome").toString() +" "+ (list.get(i) as JSONObject).get("Cognome").toString(),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                        }
                                    }
                                }
                                Column(modifier=Modifier.width((configuration.screenWidthDp/2).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    for (i in 1..list.length()-1 step 2){
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
                                            var isFavorite =(list.get(i) as JSONObject).get("ID").toString() in listFavourite
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(48.dp)
                                                            .clip(CircleShape)
                                                            .clickable { profileViewModel.set((list.get(i) as JSONObject).get("Email").toString()); goToProfile() }
                                                    ) {
                                                        val scale = if((list.get(i) as JSONObject).get("Img").toString()=="profilo.jpg"){1.0f}else{1.8f}
                                                        Image(
                                                            painter = rememberImagePainter(
                                                                data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (list.get(
                                                                    i
                                                                ) as JSONObject).get("Img")
                                                                    .toString(),
                                                            ),
                                                            contentDescription = "Immagine del profilo",
                                                            modifier = Modifier
                                                                .size(48.dp)
                                                                .clip(CircleShape)
                                                                .scale(scale)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(med+30.dp))
                                                    IconButton(
                                                        onClick = {
                                                            if(isFavorite){
                                                                favouriteViewModel.deletePref((list.get(i) as JSONObject).get("ID").toString(), em)
                                                                listFavourite.remove((list.get(i) as JSONObject).get("ID").toString())
                                                            } else {
                                                                favouriteViewModel.insert(Favourite(System.currentTimeMillis().toString(), em, (list.get(i) as JSONObject).get("ID").toString()))
                                                                listFavourite.add((list.get(i) as JSONObject).get("ID").toString())
                                                            }
                                                            isFavorite = !isFavorite
                                                        },
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
                                                    text =  (list.get(i) as JSONObject).get("Data").toString(),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = (list.get(i) as JSONObject).get("Nome").toString() +" "+ (list.get(i) as JSONObject).get("Cognome").toString(),
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier
) {
    Text(text = "Mettere le statistiche")

}




