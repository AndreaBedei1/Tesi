package com.example.seawatch

import android.Manifest
import android.app.Activity
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
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.seawatch.ui.theme.SeaWatchTheme
import dagger.hilt.android.AndroidEntryPoint

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
                    /**NavigationApp(radioOptions = radioOptions, theme = theme, settingsViewModel =  settingsViewModel, sharedPrefForLogin=sharedPrefForLogin)*/

                    Button(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(0.dp),
                        onClick = { requestCameraPermission()   }) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Vedi dettagli specie"
                        )
                    }
                }
            }
        }
    }

    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 200 && data != null){
            val imageBitmap = data.extras?.get("data") as Bitmap
            saveImageToGallery(imageBitmap)
        }
    }

    private fun saveImageToGallery(image: Bitmap) {
        val saveUri: Uri?
        val filename = "${System.currentTimeMillis()}.jpg"

        // Definisci il percorso della cartella di salvataggio dell'immagine
        val imagesCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        // Crea un contenuto Values ​​object per l'immagine
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
            Toast.makeText(this, "Immagine salvata nella galleria", Toast.LENGTH_SHORT).show()
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

    private fun requestCameraPermission() {
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
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                capturePhoto()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Toast.makeText(this, "Il permesso è necessario per accedere alla galleria", Toast.LENGTH_SHORT).show()
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                // Il permesso non è stato ancora richiesto, richiedilo all'utente
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            capturePhoto()
        } else {
            Toast.makeText(this, "Il permesso è necessario per accedere alla galleria", Toast.LENGTH_SHORT).show()
        }
    }
}
