package com.example.basicapp

import java.sql.DriverManager
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.*

fun main(){
    MovieImporter().import()

}

class MovieImporter {
    fun import() {
        val apiKey = "7895ffbe-4776-4ff0-93ac-e2241ad7096a"
        val client = OkHttpClient()
        val gson = Gson()

        val db = DriverManager.getConnection("jdbc:sqlite:movies.db")
        val statement = db.createStatement()
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS movies (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                kinopoiskId INTEGER,
                nameRu TEXT,
                nameOriginal TEXT,
                countries TEXT,
                genres TEXT,
                ratingKinopoisk REAL,
                ratingImdb REAL,
                year INTEGER,
                posterUrl TEXT,
                description TEXT
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
                val nameRu = movie["nameRu"]?.takeIf { !it.isJsonNull }?.asString
                val nameOriginal = movie["nameOriginal"]?.takeIf { !it.isJsonNull }?.asString
                val ratingKinopoisk = movie["ratingKinopoisk"]?.takeIf { !it.isJsonNull }?.asDouble
                val ratingImdb = movie["ratingImdb"]?.takeIf { !it.isJsonNull }?.asDouble
                val year = movie["year"]?.takeIf { !it.isJsonNull }?.asInt
                val posterUrl = movie["posterUrl"]?.takeIf { !it.isJsonNull }?.asString
                val description = movie["description"]?.takeIf { !it.isJsonNull }?.asString

                val countries = movie["countries"]?.asJsonArray?.joinToString(", ") {
                    it.asJsonObject["country"].asString
                }

                val genres = movie["genres"]?.asJsonArray?.joinToString(", ") {
                    it.asJsonObject["genre"].asString
                }

                val insert = db.prepareStatement("""
                    INSERT INTO movies (
                        kinopoiskId, nameRu, nameOriginal, countries, genres, 
                        ratingKinopoisk, ratingImdb, year, posterUrl, description
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent())

                insert.setObject(1, kinopoiskId)
                insert.setObject(2, nameRu)
                insert.setObject(3, nameOriginal)
                insert.setObject(4, countries)
                insert.setObject(5, genres)
                insert.setObject(6, ratingKinopoisk)
                insert.setObject(7, ratingImdb)
                insert.setObject(8, year)
                insert.setObject(9, posterUrl)
                insert.setObject(10, description)
                insert.executeUpdate()
            }
        }

        println("Фильмы успешно сохранены в базу данных.")
        db.close()
    }

}