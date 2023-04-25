package com.example.seawatch

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.OrientationEventListener
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import coil.compose.rememberImagePainter
import com.example.seawatch.data.*
import com.example.seawatch.ui.theme.SeaWatchTheme
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

public var lastImageBitmap: Bitmap? = null
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    val avvistamentiViewModel by viewModels<AvvistamentiViewModel> {
        ViewModelFactory(repository=(application as SWApplication).repository)
    }

    val favouriteViewModel by viewModels<FavouriteViewModel> {
        FavouriteViewModelFactory(repository=(application as SWApplication).repository2)
    }

    val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(repository=(application as SWApplication).repository3)
    }

    val avvistamentiViewViewModel by viewModels<AvvistamentiViewViewModel> {
        AvvistamentiViewModelFactory(repository=(application as SWApplication).repository4)
    }

    val descriptionViewModel by viewModels<DescriptionViewModel> {
        DescriptionViewModelFactory(repository=(application as SWApplication).repository5)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = false

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    public var showSnackBar = mutableStateOf(false)
    public var showAlertDialog = mutableStateOf(false)
    public val location = mutableStateOf(LocationDetails(0.toDouble(), 0.toDouble()))

    override fun onCreate(savedInstanceState: Bundle?) {
        descriptionViewModel.deleteAll()
        descriptionViewModel.populate()
        super.onCreate(savedInstanceState)
        val sharedPrefForLogin=getPreferences(Context.MODE_PRIVATE)
        lateinit var logo: ImageView

        getDatesFromServer(avvistamentiViewViewModel, this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                showSnackBar.value = true
            }
        }

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                location.value = LocationDetails(
                    p0.locations.last().latitude,
                    p0.locations.last().longitude
                )
                stopLocationUpdates()
                requestingLocationUpdates = false
            }
        }
        var coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        coroutineScope.launch {
            val avvistamenti = runBlocking { avvistamentiViewModel.all.first() }
            uploadToServer(applicationContext, avvistamenti, avvistamentiViewViewModel, avvistamentiViewModel)
        }

        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            val listItems by favouriteViewModel.all.collectAsState(initial = listOf())
            SeaWatchTheme(darkTheme = theme == getString(R.string.dark_theme)) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val radioOptions = listOf(getString(R.string.light_theme), getString(R.string.dark_theme))
                    NavigationApp(radioOptions = radioOptions,
                        theme = theme,
                        settingsViewModel =  settingsViewModel,
                        sharedPrefForLogin=sharedPrefForLogin,
                        avvistamentiViewModel=avvistamentiViewModel,
                        favouriteViewModel=favouriteViewModel,
                        listItems=listItems,
                        userViewModel=userViewModel,
                        avvistamentiViewViewModel=avvistamentiViewViewModel,
                        descriptionViewModel=descriptionViewModel
                    )
                }
            }
        }
    }

    private var name: String? = null
    private var count: Int? = null

    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 200)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 200 && data != null){
            lastImageBitmap = data.extras?.get("data") as Bitmap

            CoroutineScope(Dispatchers.Main).launch {
                saveImageToGallery(data.extras?.get("data") as Bitmap, name, count)
                recreate()
            }
        }
    }

    private fun saveImageToGallery(image: Bitmap, label:String?, c:Int?) {
        val saveUri: Uri?
        val filename = "$label$c.jpg"

        // Definisci il percorso della cartella di salvataggio dell'immagine
        val imagesCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        // Crea un contenuto Values object per l'immagine
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, image.width)
            put(MediaStore.Images.Media.HEIGHT, image.height)
        }

        // Aggiungi l'immagine alla galleria
        val resolver = applicationContext.contentResolver
        val uri = resolver.insert(imagesCollection, contentValues)

        uri?.let {
            saveUri = it
            resolver.openOutputStream(uri)?.use { outputStream ->
                // Salva l'immagine nell'OutputStream
                image.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
            }
        }
    }

    fun getDatesFromServer(avvistamentiViewViewModel: AvvistamentiViewViewModel, context:Context){
        if(isNetworkAvailable(context)){
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
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    var temp = JSONArray(body)

                    avvistamentiViewViewModel.deleteAll()
                    for (i in 0 until temp.length() step 1) {
                        avvistamentiViewViewModel.insert(
                            AvvistamentiDaVedere(
                                (temp.get(i) as JSONObject).get("ID").toString(),
                                (temp.get(i) as JSONObject).get("Email").toString(),
                                (temp.get(i) as JSONObject).get("Data").toString(),
                                (temp.get(i) as JSONObject).get("Numero_Esemplari").toString(),
                                (temp.get(i) as JSONObject).get("Latid").toString(),
                                (temp.get(i) as JSONObject).get("Long").toString() ,
                                (temp.get(i) as JSONObject).get("Anima_Nome").toString(),
                                (temp.get(i) as JSONObject).get("Specie_Nome").toString(),
                                (temp.get(i) as JSONObject).get("Mare").toString(),
                                (temp.get(i) as JSONObject).get("Vento").toString(),
                                (temp.get(i) as JSONObject).get("Note").toString(),
                                (temp.get(i) as JSONObject).get("Img").toString(),
                                (temp.get(i) as JSONObject).get("Nome").toString(),
                                (temp.get(i) as JSONObject).get("Cognome").toString(),
                                true
                            )
                        )
                    }
                }
            })
        }
    }

    /** Controllo permessi camera*/
    private val requestPermissionLauncherCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if(Build.VERSION.SDK_INT < 28){
                requestStoragePermission()
            } else {
                capturePhoto()
            }
        } else {
            // Il permesso è stato negato, mostra un messaggio all'utente
            Toast.makeText(this, "Il permesso è necessario per accedere alla fotocamera!", Toast.LENGTH_SHORT).show()
        }
    }

    public fun requestCameraPermission(id : String, c: Int=0) {
        name = id
        count = c
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                if(Build.VERSION.SDK_INT < 28){
                    requestStoragePermission()
                } else {
                    capturePhoto()
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // L'utente ha negato il permesso in precedenza, mostra un messaggio con una spiegazione
                requestPermissionLauncherCamera.launch(Manifest.permission.CAMERA)
            }
            else -> {
                // Il permesso non è stato ancora richiesto, richiedilo all'utente
                requestPermissionLauncherCamera.launch(Manifest.permission.CAMERA)
            }
        }
    }


    /** Permessi per il salvataggio in memoria */
    private fun requestStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                capturePhoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.MANAGE_EXTERNAL_STORAGE) -> {
                requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }
            else -> {
                // Il permesso non è stato ancora richiesto, richiedilo all'utente
                requestPermissionLauncher.launch(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            capturePhoto()
        } else {
            Toast.makeText(this, "Il permesso è necessario per accedere alla fotocamera!", Toast.LENGTH_SHORT).show()
        }
    }

    /** Prendo tutte le immaigni salvate */
    public fun getAllSavedImages(label: String?): List<Uri> {
        val imagesList = mutableListOf<Uri>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT
        )
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%$label%")
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN}"
        applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imagesList.add(contentUri)
            }
        }
        return imagesList
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    public fun startLocationUpdates() {
        requestingLocationUpdates = true
        val permission = Manifest.permission.ACCESS_COARSE_LOCATION

        when {
            //permission already granted
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                val gpsEnabled = checkGPS()
                if (gpsEnabled) {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                } else {
                    showAlertDialog.value = true
                }

            }
            //permission already denied
            shouldShowRequestPermissionRationale(permission) -> {
                showSnackBar.value = true
            }
            else -> {
                //first time: ask for permissions
                locationPermissionRequest.launch(
                    permission
                )
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkGPS(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


}


