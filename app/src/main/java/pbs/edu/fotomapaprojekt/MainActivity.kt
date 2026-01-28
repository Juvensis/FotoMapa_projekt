package pbs.edu.fotomapaprojekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pbs.edu.fotomapaprojekt.model.PhotoStorage
import pbs.edu.fotomapaprojekt.navigation.PhotoNavigation
import pbs.edu.fotomapaprojekt.ui.theme.FotoMapaProjektTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pozwala treści wypełnić cały ekran (pod paskami systemowymi)
        enableEdgeToEdge()

        // KROK 10: Wczytujemy zapisane zdjęcia z pliku JSON przy starcie [cite: 35, 36]
        PhotoStorage.loadData(this)

        setContent {
            FotoMapaProjektTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Uruchomienie nawigacji [cite: 32, 74]
                    PhotoNavigation()
                }
            }
        }
    }
}