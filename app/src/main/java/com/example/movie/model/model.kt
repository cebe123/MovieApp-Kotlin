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


data class WeatherResponse(
    val main: Main,
    val name: String
)

data class Main(
    val temp: Double,
)