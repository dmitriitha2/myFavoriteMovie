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
        val cursor = db.rawQuery("SELECT * FROM movies", null)

        if (cursor.moveToFirst()) {
            do {
                val nameRu = cursor.getString(cursor.getColumnIndexOrThrow("titleRus"))
                val nameOriginal = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("titleOrig"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("release_year"))
                val rating = cursor.getFloat(cursor.getColumnIndexOrThrow("ratingKinopoisk"))
                val ageRating = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("age_rating"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                movies.add(Movie(nameRu, nameOriginal, image, year, rating, ageRating, description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return movies
    }
}
