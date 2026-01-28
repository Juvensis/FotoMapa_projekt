package pbs.edu.fotomapaprojekt.model

data class PhotoEntry(
    val id: String,
    val title: String,
    val filePath: String, // Zmieniono z Uri na String
    val latitude: Double,
    val longitude: Double,
    val dateTime: String
)