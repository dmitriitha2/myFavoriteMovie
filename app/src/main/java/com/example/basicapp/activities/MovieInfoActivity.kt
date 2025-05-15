package com.example.basicapp.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.basicapp.R

class MovieInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        val titleRus: TextView = findViewById(R.id.movie_info_title)
        val image: ImageView = findViewById(R.id.movie_info_image)
        val desc: TextView = findViewById(R.id.movie_info_desc)
        val titleEng: TextView = findViewById(R.id.movie_info_title_eng)
        val year: TextView = findViewById(R.id.movie_info_year)
        val age: TextView = findViewById(R.id.movie_info_age)
        val rating: TextView = findViewById(R.id.movie_info_rating)


        titleRus.text = intent.getStringExtra("MovieTitle")
        desc.text = intent.getStringExtra("MovieDesc")
        titleEng.text = intent.getStringExtra("MovieTitleEng")
        year.text = intent.getStringExtra("MovieYear")+" г."
        rating.text = intent.getStringExtra("MovieRating")

        val movieAge = intent.getStringExtra("MovieAge")
        age.text =
            if (movieAge != null && movieAge.length >= 4) {
                movieAge.substring(3) + "+"
            } else {
                "-"
            }


        val movieImageName = intent.getStringExtra("MovieImage")
        if (movieImageName != null) {
            val imageResId = resources.getIdentifier(movieImageName, "drawable", packageName)
            if (imageResId != 0) {
                image.setImageResource(imageResId)
            }
        }

        var dialog: AlertDialog? = null

        image.setOnClickListener {
            if (dialog?.isShowing == true) {
                dialog?.dismiss() // Закрыть при повторном нажатии
            } else {
                val dialogView = layoutInflater.inflate(R.layout.dialog_image, null)
                val imageInDialog = dialogView.findViewById<ImageView>(R.id.full_image)

                // Передаём изображение
                imageInDialog.setImageDrawable(image.drawable)

                // Закрытие по нажатию на изображение
                imageInDialog.setOnClickListener {
                    dialog?.dismiss()
                }

                dialog = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create()

                dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog?.show()
            }
        }
    }
}