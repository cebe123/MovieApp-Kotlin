package com.example.movie.model

/**
 * @property title The title of the movie.
 */
data class Movie(
    val title: String
)

/**
 * @property results A list of [Movie] objects.
 */
data class Posts(
    val results: List<Movie>
)
