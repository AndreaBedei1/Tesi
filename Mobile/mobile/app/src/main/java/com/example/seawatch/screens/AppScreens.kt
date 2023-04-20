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
import coil.compose.rememberAsyncImagePainter
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

@Composable
fun takeDatasList (avvistamentiViewModel: AvvistamentiViewModel, avvistamentiViewViewModel: AvvistamentiViewViewModel): List<AvvistamentiDaVedere>{
    val temp: List<AvvistamentiDaVedere> by avvistamentiViewViewModel.all.collectAsState(initial = listOf())
    val tempAvvLocali: List<AvvistamentiDaCaricare> by avvistamentiViewModel.all.collectAsState(initial = listOf())
    val l = mutableListOf<AvvistamentiDaVedere>()
    for(e in tempAvvLocali){
        l.add(AvvistamentiDaVedere(e.id, e.avvistatore, e.data, e.numeroEsemplari, try{e.posizione.split(" ")[0]}catch (e: Exception){""}, try{e.posizione.split(" ")[1]}catch (e: Exception){""}, e.animale, e.specie, e.mare, e.vento, e.note, "profilo.jpg", "nome", "cognome", false))
    }
    return l + temp
}

data class MarkerData(
    val latitude: String,
    val longitude: String,
    val data: String,
    val animale: String,
    val specie: String
)

var sightingID: AvvistamentiDaVedere? = null
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
    profileViewModel: ProfileViewModel,
    avvistamentiViewViewModel: AvvistamentiViewViewModel,
    avvistamentiViewModel : AvvistamentiViewModel,
    userViewModel: UserViewModel
) {
    val configuration = LocalConfiguration.current
    val min = configuration.screenHeightDp.dp / 40
    val med = configuration.screenHeightDp.dp / 20
    val hig = configuration.screenHeightDp.dp / 10
    val backGround = MaterialTheme.colorScheme.primaryContainer
    val context = LocalContext.current
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }
    val options by rememberSaveable { mutableStateOf(getAnimal()) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var filterPref by rememberSaveable { mutableStateOf(false) }
    var filterAnima by rememberSaveable { mutableStateOf("") }
    var listFavourite by rememberSaveable {mutableStateOf(mutableListOf<String>())}
    var mapSet by  rememberSaveable { mutableStateOf(false) }


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
                                    TextButton(onClick = { showFilterDialog = false; filterPref=favoriteFilter; filterAnima=selectedOptionText}) {
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
        var list = mutableListOf<AvvistamentiDaVedere>()
        val temp = takeDatasList(avvistamentiViewModel, avvistamentiViewViewModel)

        for (e in temp){
            if (filterAnima == "" && !filterPref) {
                list.add(e)
            } else if (filterAnima != "" && !filterPref) {
                if(e.animale==filterAnima){
                    list.add(e)
                }
            } else if(filterAnima == "" && filterPref) {
                if(e.id in listFavourite){
                    list.add(e)
                }
            } else {
                if(e.animale==filterAnima && e.id in listFavourite){
                    list.add(e)
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
                                        loadUrl("file:///android_asset/leaflet/index.html")

                                        webViewClient = object : WebViewClient() {
                                            override fun onPageFinished(view: WebView?, url: String?) {
                                                super.onPageFinished(view, url)
                                                var mkList = mutableListOf<MarkerData>()
                                                for (e in list) {
                                                    mkList.add(
                                                        MarkerData(
                                                            e.latid,
                                                            e.long,
                                                            e.data,
                                                            e.animale,
                                                            e.specie
                                                        )
                                                    )
                                                }
                                                val gson = Gson()
                                                var markerDataJson = gson.toJson(mkList)
                                                var currentMarkerDataJson = markerDataJson
                                                mapSet=true
                                                try {
                                                    view?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                                } catch (e: Exception) {
                                                }
                                            }
                                        }
                                    }
                                },
                                update = { webView ->
                                    if(mapSet) {
                                        try {
                                            webView?.evaluateJavascript("removeMarkers()", null)
                                            var mkList = mutableListOf<MarkerData>()
                                            for (e in list) {
                                                mkList.add(
                                                    MarkerData(
                                                        e.latid,
                                                        e.long,
                                                        e.data,
                                                        e.animale,
                                                        e.specie
                                                    )
                                                )
                                            }
                                            val gson = Gson()
                                            var markerDataJson = gson.toJson(mkList)
                                            var currentMarkerDataJson = markerDataJson
                                            webView?.evaluateJavascript(
                                                "addMarkers('$currentMarkerDataJson')",
                                                null
                                            )
                                        } catch (e: Exception) {
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    LazyColumn(modifier = modifier.padding(vertical = 10.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                        items(1){element->
                            Spacer(modifier = Modifier.height(30.dp))
                            Spacer(modifier = Modifier.height(min))
                            Text(
                                text = "Avvistamenti",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(min/2))
                            Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                                for(k in 0..1){
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.width((configuration.screenWidthDp/4).dp)) {
                                        for (i in k..list.count()-1 step 2) {
                                            var nome by rememberSaveable { mutableStateOf(list.get(i).nome) }
                                            var cognome by rememberSaveable { mutableStateOf(list.get(i).cognome) }
                                            var img by rememberSaveable { mutableStateOf(list.get(i).img) }
                                            if(!list.get(i).online){
                                                if (isNetworkAvailable(context)) {
                                                    val client = OkHttpClient()
                                                    val formBody = MultipartBody.Builder()
                                                        .setType(MultipartBody.FORM)
                                                        .addFormDataPart("user", list.get(i).avvistatore)
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
                                                            try {
                                                                nome = (msg.get(0) as JSONObject).get("Nome").toString()
                                                                cognome = (msg.get(0) as JSONObject).get("Cognome").toString()
                                                                img = (msg.get(0) as JSONObject).get("Img").toString()
                                                                list.get(i).nome = nome
                                                                list.get(i).cognome = cognome
                                                            } catch (e: Exception) {
                                                            }
                                                        }
                                                    })
                                                } else {
                                                    val userItems: List<User> by userViewModel.all.collectAsState(initial = listOf())
                                                    for(elem in userItems){
                                                        if(elem.mail == list.get(i).avvistatore){
                                                            nome = elem.nome
                                                            cognome = elem.cognome
                                                            list.get(i).nome = nome
                                                            list.get(i).cognome = cognome
                                                            break
                                                        }
                                                    }
                                                }
                                            } else {
                                                val userItems: List<User> by userViewModel.all.collectAsState(initial = listOf())
                                                for(elem in userItems){
                                                    if(elem.mail == list.get(i).avvistatore){
                                                        nome = elem.nome
                                                        cognome = elem.cognome
                                                        list.get(i).nome = nome
                                                        list.get(i).cognome = cognome
                                                        break
                                                    }
                                                }
                                            }
                                            Card(
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier
                                                    .padding(5.dp)
                                                    .size(width = 180.dp, height = 160.dp),
                                                border = BorderStroke(2.dp, Color.Black),
                                                colors = CardDefaults.cardColors(containerColor = if(list.get(i).online){MaterialTheme.colorScheme.secondaryContainer} else {MaterialTheme.colorScheme.tertiaryContainer}),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                onClick = {
                                                    sightingID = list.get(i)
                                                    goToSighting()
                                                }
                                            ) {
                                                var isFavorite =list.get(i).id in listFavourite
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
                                                                .clickable {
                                                                    profileViewModel.set(list.get(i).avvistatore)
                                                                    goToProfile()
                                                                }
                                                        ) {
                                                            val scale = if(img=="profilo.jpg" || !isNetworkAvailable(context)){1.0f}else{1.8f}
                                                            Image(
                                                                painter = rememberImagePainter(
                                                                    data = if(isNetworkAvailable(context)){
                                                                        "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + img
                                                                    } else {
                                                                        R.drawable.profilo
                                                                    },
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
                                                                    favouriteViewModel.deletePref(list.get(i).id, em)
                                                                    listFavourite.remove(list.get(i).id)
                                                                } else {
                                                                    favouriteViewModel.insert(Favourite(System.currentTimeMillis().toString(), em, list.get(i).id))
                                                                    listFavourite.add(list.get(i).id)
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
                                                        text = list.get(i).data.substring(0,16),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = list.get(i).animale,
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Text(
                                                        text = nome + " " + cognome,
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
                                        loadUrl("file:///android_asset/leaflet/index.html")

                                        webViewClient = object : WebViewClient() {
                                            override fun onPageFinished(view: WebView?, url: String?) {
                                                try {
                                                    super.onPageFinished(view, url)
                                                    var mkList = mutableListOf<MarkerData>()
                                                    for (e in list) {
                                                        mkList.add(
                                                            MarkerData(
                                                                e.latid,
                                                                e.long,
                                                                e.data,
                                                                e.animale,
                                                                e.specie
                                                            )
                                                        )
                                                    }
                                                    val gson = Gson()
                                                    var markerDataJson = gson.toJson(mkList)
                                                    var currentMarkerDataJson = markerDataJson
                                                    mapSet = true
                                                    view?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                                } catch (e: Exception) {
                                                }
                                            }
                                        }
                                    }
                                },
                                update = { webView ->
                                    if(mapSet) {
                                        try {
                                            webView?.evaluateJavascript("removeMarkers()", null)
                                            var mkList = mutableListOf<MarkerData>()
                                            for (e in list) {
                                                mkList.add(
                                                    MarkerData(
                                                        e.latid,
                                                        e.long,
                                                        e.data,
                                                        e.animale,
                                                        e.specie
                                                    )
                                                )
                                            }
                                            val gson = Gson()
                                            var markerDataJson = gson.toJson(mkList)
                                            var currentMarkerDataJson = markerDataJson
                                            webView?.evaluateJavascript(
                                                "addMarkers('$currentMarkerDataJson')",
                                                null
                                            )
                                        } catch (e: Exception) {
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(min))
                        Text(
                            text = "Avvistamenti",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(min/2))
                        Row(modifier=Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                            for(k in 0..1){
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.width((configuration.screenWidthDp/2).dp)) {
                                    for (i in k..list.count()-1 step 2) {
                                        var nome by rememberSaveable { mutableStateOf(list.get(i).nome) }
                                        var cognome by rememberSaveable { mutableStateOf(list.get(i).cognome) }
                                        var img by rememberSaveable { mutableStateOf(list.get(i).img) }
                                        if(!list.get(i).online){
                                            if (isNetworkAvailable(context)) {
                                                val client = OkHttpClient()
                                                val formBody = MultipartBody.Builder()
                                                    .setType(MultipartBody.FORM)
                                                    .addFormDataPart("user", list.get(i).avvistatore)
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
                                                        try {
                                                            nome = (msg.get(0) as JSONObject).get("Nome").toString()
                                                            cognome = (msg.get(0) as JSONObject).get("Cognome").toString()
                                                            img = (msg.get(0) as JSONObject).get("Img").toString()
                                                            list.get(i).nome = nome
                                                            list.get(i).cognome = cognome
                                                        } catch (e: Exception) {
                                                        }
                                                    }
                                                })
                                            } else {
                                                val userItems: List<User> by userViewModel.all.collectAsState(initial = listOf())
                                                for(elem in userItems){
                                                    if(elem.mail == list.get(i).avvistatore){
                                                        nome = elem.nome
                                                        cognome = elem.cognome
                                                        list.get(i).nome = nome
                                                        list.get(i).cognome = cognome
                                                        break
                                                    }
                                                }
                                            }
                                        } else {
                                            val userItems: List<User> by userViewModel.all.collectAsState(initial = listOf())
                                            for(elem in userItems){
                                                if(elem.mail == list.get(i).avvistatore){
                                                    nome = elem.nome
                                                    cognome = elem.cognome
                                                    list.get(i).nome = nome
                                                    list.get(i).cognome = cognome
                                                    break
                                                }
                                            }
                                        }
                                        Card(
                                            shape = MaterialTheme.shapes.medium,
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .size(width = 180.dp, height = 160.dp),
                                            border = BorderStroke(2.dp, Color.Black),
                                            colors = CardDefaults.cardColors(containerColor = if(list.get(i).online){MaterialTheme.colorScheme.secondaryContainer} else {MaterialTheme.colorScheme.tertiaryContainer}),
                                            elevation = CardDefaults.cardElevation(4.dp),
                                            onClick = {
                                                sightingID = list.get(i)
                                                goToSighting()
                                            }
                                        ) {
                                            var isFavorite =list.get(i).id in listFavourite
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
                                                            .clickable {
                                                                profileViewModel.set(list.get(i).avvistatore)
                                                                goToProfile()
                                                            }
                                                    ) {
                                                        val scale = if(img=="profilo.jpg" || !isNetworkAvailable(context)){1.0f}else{1.8f}
                                                        Image(
                                                            painter = rememberImagePainter(
                                                                data = if(isNetworkAvailable(context)){
                                                                    "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + img
                                                                } else {
                                                                    R.drawable.profilo
                                                                },
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
                                                                favouriteViewModel.deletePref(list.get(i).id, em)
                                                                listFavourite.remove(list.get(i).id)
                                                            } else {
                                                                favouriteViewModel.insert(Favourite(System.currentTimeMillis().toString(), em, list.get(i).id))
                                                                listFavourite.add(list.get(i).id)
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
                                                    text = list.get(i).data.substring(0,16),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = list.get(i).animale,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = nome + " " + cognome,
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
                        val painter = rememberAsyncImagePainter(uri.toString())
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
                                    text = "Prova123",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Dimensione:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Lunghezza massima: 6,4 m maschio; età massima riportata: 36 anni.",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Descrizione:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Distribuzione: Cosmopolita, prevalentemente anfitemperato. Atlantico occidentale: Terranova, Canada fino all'Argentina; anche nord del Golfo del Messico, Bahamas, Cuba e Piccole Antille. Atlantico orientale: Dalla Francia al Sudafrica, incluso il Mediterraneo. Oceano Indiano: Seychelles, Sudafrica; anche Reunion e Mauritius. Pacifico occidentale: dalla Siberia alla Nuova Zelanda e alle Isole Marshall; anche Australia meridionale. Pacifico centrale: Hawaii. Pacifico orientale: Alaska fino al Cile. Aspetto: Squalo di grandi dimensioni, a forma di fuso, con vistosi occhi neri, muso smussato e conico e denti grandi, triangolari e seghettati. L'origine della prima pinna dorsale si trova solitamente sopra i margini interni della pinna pettorale. Pinna caudale a forma di luna crescente. Colore da grigio piombo a marrone o nero sopra, più chiaro sui fianchi e bruscamente bianco sotto. Macchia nera alla base della pinna pettorale posteriore. Biologia: Abitante principalmente delle piattaforme continentali e insulari, ma può essere presente anche al largo di isole oceaniche lontane dalla terraferma. Spesso si avvicina alla linea di costa e penetra anche in baie poco profonde. Pelagico, capace di migrare attraverso le regioni oceaniche. Solitamente solitario o in coppia, ma può trovarsi in aggregazioni alimentari di 10 o più esemplari; non forma banchi. Si nutre di pesci ossei, squali, razze, foche, delfini e focene, uccelli marini, carogne, calamari, polpi e granchi e balene. Secondo alcuni esperti, attacca l'uomo scambiandolo per la sua normale preda. La maggior parte degli attacchi avviene negli estuari. La lunghezza massima totale ha dato adito a molte speculazioni e alcune misurazioni sono risultate dubbie. Forse fino a 6,4 m o più di lunghezza. Considerato il più grande predatore del mondo con un ampio spettro di prede. Stato di conservazione Lista Rossa IUCN Vulnerabile, vedi Lista Rossa IUCN (VU)",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Curiosità:",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Ciclo vitale e comportamento di accoppiamento: Presenta ovoviparità (viviparità aplacentare), con embrioni che si nutrono di altri ovuli prodotti dalla madre (oofagia) dopo l'assorbimento del sacco vitellino. Fino a 10, forse 14, piccoli nati a 120-150 cm.",
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




