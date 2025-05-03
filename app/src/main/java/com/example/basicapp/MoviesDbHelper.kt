package com.example.basicapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("kinopoiskId"))
                val nameOriginal = cursor.getString(cursor.getColumnIndexOrThrow("nameOriginal"))
                val nameRu = cursor.getString(cursor.getColumnIndexOrThrow("nameRu"))
                val country = cursor.getString(cursor.getColumnIndexOrThrow("countries"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                val genre = cursor.getString(cursor.getColumnIndexOrThrow("genres"))
                val rating = cursor.getFloat(cursor.getColumnIndexOrThrow("ratingKinopoisk"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                movies.add(Movie(id, nameOriginal, nameRu, country, year, genre, rating, description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return movies
    }
}
