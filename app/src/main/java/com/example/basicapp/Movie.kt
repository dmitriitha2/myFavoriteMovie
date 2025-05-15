package com.example.basicapp

data class Movie(
    val kinopoiskId: Int,
    val titleRus: String,
    val titleOrig: String?,
    val image: String,
    val year: String?,
    val countries: String?,
    val genres: String?,
    val rating: String?,
    val ageRating: String?,
    val description: String?
)
