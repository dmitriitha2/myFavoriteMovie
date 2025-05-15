package com.example.basicapp

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basicapp.activities.MovieInfoActivity
import java.sql.DriverManager

class MoviesAdapter(var movies: List<Movie>, var context: Context): RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.movie_image)
        val title: TextView = view.findViewById(R.id.movie_title)
        val country: TextView = view.findViewById(R.id.movie_country)
        val year: TextView = view.findViewById(R.id.movie_year)
        val genre: TextView = view.findViewById(R.id.movie_genre)
        val rating: TextView = view.findViewById(R.id.movie_rating)

        val layout: LinearLayout = view.findViewById(R.id.clickable_layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val title = movies[position].titleRus
        val countries = movies[position].countries
        val year = movies[position].year
        val genres = movies[position].genres
        val rating = movies[position].rating

        holder.title.text = if (title.length <= 23) title else title.substring(0, 18) + "…"

        holder.country.text =
            if (countries != null) {
                if (countries.length <= 18){

                    countries + ","
                } else

                    countries.substring(0, 18) + "…"
        } else ""


        holder.year.text = year ?: ""

        holder.genre.text =
            if (genres != null) {
             if (genres.length <= 25) {
                 genres + ","
             } else genres.substring(0, 25) + "…"
        } else ""

        holder.rating.text = rating ?: ""


        val imageId = context.resources.getIdentifier(
            movies[position].image,
            "drawable",
            context.packageName
        )
        holder.image.setImageResource(imageId)

        holder.layout.setOnClickListener {
            val intent = Intent(context, MovieInfoActivity::class.java)
            intent.putExtra("MovieTitle", movies[position].titleRus)
            intent.putExtra("MovieImage", movies[position].image)
            intent.putExtra("MovieDesc", movies[position].description)
            intent.putExtra("MovieTitleEng", movies[position].titleOrig)
            intent.putExtra("MovieYear", movies[position].year)
            intent.putExtra("MovieAge", movies[position].ageRating)
            intent.putExtra("MovieRating", movies[position].rating)
            context.startActivity(intent)
        }
    }

    fun updateList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}