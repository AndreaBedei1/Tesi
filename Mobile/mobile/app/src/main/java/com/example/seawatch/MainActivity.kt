package com.example.seawatch

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.setContent
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
import com.example.seawatch.ui.theme.SeaWatchTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefForLogin=getPreferences(Context.MODE_PRIVATE)

        lateinit var logo: ImageView
        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            SeaWatchTheme(darkTheme = theme == getString(R.string.dark_theme)) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val radioOptions = listOf(getString(R.string.light_theme), getString(R.string.dark_theme))
                    NavigationApp(radioOptions = radioOptions, theme = theme, settingsViewModel =  settingsViewModel, sharedPrefForLogin=sharedPrefForLogin)
                }
            }
        }
    }

    fun showMap() {
        val geoLocation = Uri.parse("geo:44.1391, 12.24315")
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
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
            val imageBitmap = data.extras?.get("data") as Bitmap
            CoroutineScope(Dispatchers.Main).launch {
                saveImageToGallery(imageBitmap, name, count)
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

    /** Controllo permessi camera*/
    private val requestPermissionLauncherCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            requestStoragePermission()
        } else {
            // Il permesso è stato negato, mostra un messaggio all'utente
            Toast.makeText(this, "Il permesso è necessario per accedere alla fotocamera", Toast.LENGTH_SHORT).show()
        }
    }

    public fun requestCameraPermission(id : String, c: Int) {
        name = id
        count = c
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                requestStoragePermission()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // L'utente ha negato il permesso in precedenza, mostra un messaggio con una spiegazione
                Toast.makeText(this, "Il permesso è necessario per accedere alla fotocamera", Toast.LENGTH_SHORT).show()
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
            capturePhoto()
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
        Toast.makeText(this, "$label", Toast.LENGTH_SHORT).show()
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



}
