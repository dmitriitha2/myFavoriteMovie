package com.example.basicapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull

class MoviesDbHelper(context: Context) : SQLiteOpenHelper(context, "movies.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getAllMovies(): List<Movie> {
        val movies = mutableListOf<Movie>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT m.*, " +
                "GROUP_CONCAT(DISTINCT c.country) as countries, " +
                "GROUP_CONCAT(DISTINCT g.name) as genres " +
                "FROM movies m " +
                "LEFT JOIN movie_countries mc ON m.kinopoiskId = mc.movie_id " +
                "LEFT JOIN countries c ON mc.country_id = c.id " +
                "LEFT JOIN movie_genres mg ON m.kinopoiskId = mg.movie_id " +
                "LEFT JOIN genres g ON mg.genre_id = g.id " +
                "GROUP BY m.kinopoiskId", null)

        if (cursor.moveToFirst()) {
            do {
                val kinopoiskId = cursor.getInt(cursor.getColumnIndexOrThrow("kinopoiskId"))
                val nameRu = cursor.getString(cursor.getColumnIndexOrThrow("titleRus"))
                val nameOriginal = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("titleOrig"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val year = cursor.getString(cursor.getColumnIndexOrThrow("release_year"))
                val countries = (cursor.getString(cursor.getColumnIndexOrThrow("countries")) ?: "")
                    .split(",")
                    .joinToString(", ") { it.trim() }
                val genres = (cursor.getString(cursor.getColumnIndexOrThrow("genres"))?: "")
                    .split(",")
                    .joinToString(", ") { it.trim() }
                val rating = cursor.getString(cursor.getColumnIndexOrThrow("ratingKinopoisk")) ?: "-"
                val ageRating = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("age_rating"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                movies.add(Movie(kinopoiskId, nameRu, nameOriginal, image, year, countries, genres, rating, ageRating, description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return movies
    }
}
