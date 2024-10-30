package com.example.movie.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides Retrofit instance for API calls.
 */
object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }
}
