package com.example.seawatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Create
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.FragmentActivity
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*


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

data class MarkerData(
    val latitude: String,
    val longitude: String,
    val data: String,
    val animale: String,
    val specie: String
)

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
    val context = LocalContext.current
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var avvList by rememberSaveable { mutableStateOf("") }
    val markerList = listOf<MarkerData>()
    val gson = Gson()
    var markerDataJson = gson.toJson(markerList)
    var currentMarkerDataJson by rememberSaveable { mutableStateOf("") }

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

            var list = JSONArray()
            if(avvList!="") {
                list = JSONArray(avvList)
                var mkList = mutableListOf<MarkerData>()
                for (i in 0..list.length() - 1 step 1) {
                    if(filterAnima==""){
                        mkList.add(
                            MarkerData(
                                (list.get(i) as JSONObject).get("Latid").toString(),
                                (list.get(i) as JSONObject).get("Long").toString(),
                                (list.get(i) as JSONObject).get("Data").toString(),
                                (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                (list.get(i) as JSONObject).get("Specie_Nome").toString()
                            )
                        )
                    } else {
                        if((list.get(i) as JSONObject).get("Anima_Nome").toString() == filterAnima){
                            MarkerData(
                                (list.get(i) as JSONObject).get("Latid").toString(),
                                (list.get(i) as JSONObject).get("Long").toString(),
                                (list.get(i) as JSONObject).get("Data").toString(),
                                (list.get(i) as JSONObject).get("Anima_Nome").toString(),
                                (list.get(i) as JSONObject).get("Specie_Nome").toString()
                            )
                        }
                    }
                }
                markerDataJson = gson.toJson(mkList)
                currentMarkerDataJson = markerDataJson
            }
        }
    })

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

                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            view?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                        }
                                    }
                                }
                            },
                            update = { webView ->
                                webView.loadUrl("file:///android_asset/leaflet/index.html")
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(1){element->
                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .background(backGround),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                onClick = { }
                            ) {
                                Icon(painter = painterResource(id = R.drawable.baseline_cloud_upload_24), contentDescription = "Upload avvistamento locali")
                                Spacer(modifier = Modifier.width(7.dp))
                                Text("AVVISTAMENTI LOCALI")
                            }
                        }
                        var list = JSONArray()
                        if(avvList!=""){
                            list = JSONArray(avvList)
                        }
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
                                        var isFavorite by remember { mutableStateOf(false) }
                                        /** Cambiare in base al DB */
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = rememberImagePainter(
                                                        data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (list.get(
                                                            i
                                                        ) as JSONObject).get("Img").toString(),
                                                    ),
                                                    contentDescription = "Immagine del profilo",
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(CircleShape)
                                                )

                                                Spacer(modifier = Modifier.width(med + 30.dp))
                                                IconButton(
                                                    onClick = { isFavorite = !isFavorite },
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
                                        var isFavorite by remember { mutableStateOf(false) }
                                        /** Cambiare in base al DB */
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Image(
                                                    painter = rememberImagePainter(
                                                        data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/" + (list.get(
                                                            i
                                                        ) as JSONObject).get("Img").toString(),
                                                    ),
                                                    contentDescription = "Immagine del profilo",
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(CircleShape)
                                                )

                                                Spacer(modifier = Modifier.width(med + 30.dp))
                                                IconButton(
                                                    onClick = { isFavorite = !isFavorite },
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
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(backGround),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            onClick = {

                            }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.baseline_cloud_upload_24), contentDescription = "Upload avvistamento locali")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("AVVISTAMENTI LOCALI")
                        }
                    }
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
                                            view?.evaluateJavascript("addMarkers('$currentMarkerDataJson')", null)
                                        }
                                    }
                                }
                            },
                            update = { webView ->
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
                    var list = JSONArray()
                    if(avvList!=""){
                        list = JSONArray(avvList)
                    }
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
                                                painter = rememberImagePainter(
                                                    data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/"+(list.get(i) as JSONObject).get("Img").toString(),
                                                ),
                                                contentDescription = "Immagine del profilo",
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
                                                painter = rememberImagePainter(
                                                    data = "https://isi-seawatch.csr.unibo.it/Sito/img/profilo/"+(list.get(i) as JSONObject).get("Img").toString(),
                                                ),
                                                contentDescription = "Immagine del profilo",
                                                modifier = Modifier
                                                    .size(40.dp)
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




