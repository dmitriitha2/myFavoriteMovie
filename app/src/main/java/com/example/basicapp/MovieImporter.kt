package com.example.basicapp

import java.sql.DriverManager
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

fun main() {

    //MovieImporter().import()
    MovieImporter().downloadPosters()
}

class MovieImporter {

    val apiKey = "7895ffbe-4776-4ff0-93ac-e2241ad7096a"
    val client = OkHttpClient()
    val db = DriverManager.getConnection("jdbc:sqlite:app/src/main/assets/movies.db")
    val outputDir = File("app/src/main/res/drawable")

    fun import() {

        val statement = db.createStatement()
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS movies (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                kinopoiskId INTEGER,
                titleRus TEXT,
                titleEng TEXT,
                poster_url TEXT,
                release_year INTEGER,
                duration_min INTEGER,
                age_rating TEXT,
                director_id INTEGER
            )
        """.trimIndent())

        for (page in 1..10) {
            val request = Request.Builder()
                .url("https://kinopoiskapiunofficial.tech/api/v2.2/films/collections?type=TOP_250_MOVIES&page=$page")
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", apiKey)
                .build()

            val response = client.newCall(request).execute()
            val json = response.body?.string()

            val obj = JsonParser.parseString(json).asJsonObject
            val items = obj.getAsJsonArray("items")

            for (item in items) {
                val movie = item.asJsonObject
                val kinopoiskId = movie["kinopoiskId"]?.takeIf { !it.isJsonNull }?.asInt
                val titleRus = movie["nameRu"]?.takeIf { !it.isJsonNull }?.asString
                val titleOrig = movie["nameOriginal"]?.takeIf { !it.isJsonNull }?.asString
                val image = "a${kinopoiskId ?: 0}"
                val year = movie["year"]?.takeIf { !it.isJsonNull }?.asInt
                val ratingKinopoisk = movie["ratingKinopoisk"]?.takeIf { !it.isJsonNull }?.asDouble
                val ageRating = movie["ratingAgeLimits"]?.takeIf { !it.isJsonNull }?.asString
                val description = movie["description"]?.takeIf { !it.isJsonNull }?.asString

                //val countries = movie["countries"]?.asJsonArray?.joinToString(", ") {
                //it.asJsonObject["country"].asString
                //}

                //val genres = movie["genres"]?.asJsonArray?.joinToString(", ") {
                //it.asJsonObject["genre"].asString
                //}

                val insert = db.prepareStatement("""
                    INSERT OR IGNORE INTO movies (
                        kinopoiskId, titleRus, titleOrig, image, release_year, 
                        ratingKinopoisk, age_rating, description
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent())

                insert.setObject(1, kinopoiskId)
                insert.setObject(2, titleRus)
                insert.setObject(3, titleOrig)
                insert.setObject(4, image)
                insert.setObject(5, year)
                insert.setObject(6, ratingKinopoisk)
                insert.setObject(7, ageRating)
                insert.setObject(8, description)
                insert.executeUpdate()
            }
        }

        println("Фильмы успешно сохранены в базу данных.")
        db.close()
    }

    fun downloadPosters() {

        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        for (page in 1..10) {
            val request = Request.Builder()
                .url("https://kinopoiskapiunofficial.tech/api/v2.2/films/collections?type=TOP_250_MOVIES&page=$page")
                .addHeader("accept", "application/json")
                .addHeader("X-API-KEY", apiKey)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Ошибка при получении данных с API: ${response.code}")
                    return
                }

                val json = response.body?.string() ?: return
                val obj = JsonParser.parseString(json).asJsonObject
                val items = obj.getAsJsonArray("items")

                for (item in items) {
                    val movie = item.asJsonObject
                    val kinopoiskId = movie["kinopoiskId"]?.asInt ?: continue
                    val posterUrl = movie["posterUrl"]?.asString ?: continue
                    val fileName = "a$kinopoiskId.jpg"
                    val file = File(outputDir, fileName)

                    if (file.exists()) {
                        println("Файл $fileName уже существует, пропускаем загрузку.")
                        continue
                    }

                    try {
                        val imageBytes = URL(posterUrl).readBytes()
                        FileOutputStream(file).use { it.write(imageBytes) }
                        println("Сохранен файл: $fileName")
                    } catch (e: Exception) {
                        println("Ошибка при загрузке изображения для ID $kinopoiskId: ${e.message}")
                    }
                }
            }
        }
    }
}
