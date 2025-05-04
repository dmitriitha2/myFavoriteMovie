package com.example.basicapp

data class Movie(
    val titleRus: String,
    val titleOrig: String?,
    val image: String,
    val year: Int,
    val rating: Float,
    val ageRating: String?,
    val description: String
)
