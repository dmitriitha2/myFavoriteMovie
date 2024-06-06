package com.example.basicapp

data class Movie(
    val id: Int = 0,
    val image: String,
    val title: String,
    val country: String,
    val year: Int,
    val genre: String,
    val rating: Float,
    val description: String
)
