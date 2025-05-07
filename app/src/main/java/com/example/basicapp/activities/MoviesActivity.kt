package com.example.basicapp.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basicapp.Movie
import com.example.basicapp.MoviesAdapter
import com.example.basicapp.MoviesDbHelper
import com.example.basicapp.R
import java.io.FileOutputStream

class MoviesActivity : AppCompatActivity() {
    private lateinit var moviesAdapter: MoviesAdapter
    private var allMovies: List<Movie> = emptyList()

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
        allMovies = MoviesDbHelper(this).getAllMovies()

        moviesList.layoutManager = LinearLayoutManager(this)
        moviesAdapter = MoviesAdapter(allMovies, this)
        moviesList.adapter = moviesAdapter

        setupSearchView()
    }

    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovies(newText)
                return true
            }
        })
    }

    private fun filterMovies(query: String?) {
        if (query.isNullOrBlank()) {
            moviesAdapter.updateList(allMovies)
            return
        }

        val searchQuery = query.lowercase()
        val filteredMovies = allMovies.filter { movie ->
            movie.titleRus.lowercase().contains(searchQuery)
        }
        moviesAdapter.updateList(filteredMovies)
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