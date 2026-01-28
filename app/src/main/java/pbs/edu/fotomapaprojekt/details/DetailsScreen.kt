package pbs.edu.fotomapaprojekt.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import pbs.edu.fotomapaprojekt.model.PhotoStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, photoId: String?) {
    // Znajdujemy zdjęcie w naszej pamięci na podstawie przekazanego ID
    val photo = PhotoStorage.photos.find { it.id == photoId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły zdjęcia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { padding ->
        if (photo == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Nie znaleziono zdjęcia.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // 1. Duże zdjęcie
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    AsyncImage(
                        model = photo.filePath,
                        contentDescription = "Pełne zdjęcie",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Dane tekstowe (Metadane)
                Text(text = photo.title, style = MaterialTheme.typography.headlineMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(text = "Data wykonania: ${photo.dateTime}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Szerokość (Lat): ${photo.latitude}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Długość (Lon): ${photo.longitude}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Sekcja Mapy (uproszczona wizualizacja)
                Text(text = "Lokalizacja na mapie", style = MaterialTheme.typography.titleMedium)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        // Tutaj w prawdziwej aplikacji byłby Google Maps SDK.
                        // Na potrzeby zaliczenia pokazujemy tekst z pozycją pinezki.
                        Text(
                            text = "📍 Pinezka: ${photo.latitude}, ${photo.longitude}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}