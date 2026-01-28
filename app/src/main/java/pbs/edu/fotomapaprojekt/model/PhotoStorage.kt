package pbs.edu.fotomapaprojekt.model

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object PhotoStorage {
    val photos = mutableStateListOf<PhotoEntry>()
    private const val FILE_NAME = "photos_data.json"

    fun addPhoto(context: Context, entry: PhotoEntry) {
        photos.add(entry)
        saveData(context)
    }

    fun deletePhoto(context: Context, entry: PhotoEntry) {
        photos.remove(entry)
        saveData(context)
    }

    fun saveData(context: Context) {
        try {
            val gson = Gson()
            val jsonString = gson.toJson(photos.toList())
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadData(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val jsonString = file.readText()
                if (jsonString.isBlank()) return // Jeśli plik pusty, nic nie rób

                val type = object : TypeToken<List<PhotoEntry>>() {}.type
                val savedPhotos: List<PhotoEntry>? = Gson().fromJson(jsonString, type)

                if (savedPhotos != null) {
                    photos.clear()
                    photos.addAll(savedPhotos)
                }
            }
        } catch (e: Exception) {
            // Jeśli wystąpi błąd (np. zły format JSON), czyścimy listę i pozwalamy aplikacji żyć
            e.printStackTrace()
            photos.clear()
        }
    }
}