package com.example.movie.model

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @property title The title of the movie.
 */

data class Movie(
    val title: String,

)

/**
 * @property results A list of [Movie] objects.
 */

data class Posts(
    val results: List<Movie>
)
