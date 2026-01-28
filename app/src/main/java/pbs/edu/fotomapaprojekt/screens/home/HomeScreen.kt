package pbs.edu.fotomapaprojekt.screens.home

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import pbs.edu.fotomapaprojekt.model.PhotoEntry
import pbs.edu.fotomapaprojekt.model.PhotoStorage
import pbs.edu.fotomapaprojekt.navigation.PhotoScreens
import pbs.edu.fotomapaprojekt.utils.LocationHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    var currentFile by remember { mutableStateOf<File?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        currentFile = file // Zapamiętujemy plik, aby wyciągnąć jego ścieżkę
        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentFile != null) {
            locationHelper.getLastLocation { location ->
                val newEntry = PhotoEntry(
                    id = System.currentTimeMillis().toString(),
                    title = "Foto ${PhotoStorage.photos.size + 1}",
                    filePath = currentFile!!.absolutePath, // Zapisujemy trwałą ścieżkę
                    latitude = location?.latitude ?: 0.0,
                    longitude = location?.longitude ?: 0.0,
                    dateTime = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
                )
                PhotoStorage.addPhoto(context, newEntry)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false

        if (cameraGranted && locationGranted) {
            photoUri = createImageUri()
            cameraLauncher.launch(photoUri!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FotoMapa - Galeria") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { padding ->
        if (PhotoStorage.photos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(text = "Brak zdjęć. Kliknij +, aby dodać.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(PhotoStorage.photos) { photo ->
                    PhotoCard(
                        photo = photo,
                        onCardClick = {
                            navController.navigate(PhotoScreens.DetailsScreen.name + "/${photo.id}")
                        },
                        onDeleteClick = {
                            PhotoStorage.deletePhoto(context, photo)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoCard(photo: PhotoEntry, onCardClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp).height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = photo.filePath, // Coil świetnie radzi sobie ze ścieżką String
                contentDescription = "Miniatura",
                modifier = Modifier.size(100.dp).padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = photo.title, style = MaterialTheme.typography.titleMedium)
                Text(text = photo.dateTime, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "GPS: ${String.format("%.2f", photo.latitude)}, ${String.format("%.2f", photo.longitude)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Usuń",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}