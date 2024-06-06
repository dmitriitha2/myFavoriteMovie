package com.example.basicapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

        holder.title.text = movies[position].title
        holder.country.text = movies[position].country + ","
        holder.year.text = movies[position].year.toString() + "г."
        holder.genre.text = movies[position].genre
        holder.rating.text = "Рейтинг: " + movies[position].rating.toString()

        val imageId = context.resources.getIdentifier(
            movies[position].image,
            "drawable",
            context.packageName
        )
        holder.image.setImageResource(imageId)

        holder.layout.setOnClickListener {
            val intent = Intent(context, MovieInfoActivity::class.java)
            intent.putExtra("MovieTitle", movies[position].title)
            intent.putExtra("MovieImage", movies[position].image)
            intent.putExtra("MovieDesc", movies[position].description)
            context.startActivity(intent)
        }
    }
}