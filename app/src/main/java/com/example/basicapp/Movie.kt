package com.example.basicapp

data class Movie(
    val kinopoiskId: Int,
    val titleRus: String,
    val titleOrig: String?,
    val image: String,
    val year: Int,
    val countries: String,
    val genres: String,
    val rating: Float,
    val ageRating: String?,
    val description: String
)
