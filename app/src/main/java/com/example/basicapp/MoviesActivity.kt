package com.example.basicapp

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.FileOutputStream

class MoviesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movies)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        copyDatabase(this)
        val moviesList: RecyclerView = findViewById(R.id.movies_list)
        val dbHelper = MoviesDbHelper(this)
        val movies = dbHelper.getAllMovies()


        //val movies = arrayListOf<Movie>()
        //movies.add(Movie(1, "shawshank", "Побег из Шоушенка","США", 1994, "Драма",9.1f, getString(R.string.shawshank_desc)))
        //movies.add(Movie(2, "green_mile", "Зеленая миля","США",1999, "Драма",9.1f, getString(R.string.green_mile_desc)))
        //movies.add(Movie(3, "forrest_gump", "Форрест Гамп","США", 1994, "Драма", 8.9f, getString(R.string.forrest_gump_desc)))
        //movies.add(Movie(4, "one_plus_one", "1+1", "Франция", 2011, "Драма",8.9f, getString(R.string.one_plus_one_desc)))
        //movies.add(Movie(5, "shindlers_list", "Список Шиндлера","США", 1993, "Драма", 8.8f, getString(R.string.shindlers_desc)))

        moviesList.layoutManager = LinearLayoutManager(this)
        moviesList.adapter = MoviesAdapter(movies, this)
    }

    private fun copyDatabase(context: Context) {
        val dbName = "movies.db"
        val dbPath = context.getDatabasePath(dbName)

        if (dbPath.exists()) return

        dbPath.parentFile?.mkdirs()
        context.assets.open(dbName).use { input ->
            FileOutputStream(dbPath).use { output ->
                input.copyTo(output)
            }
        }
    }
}