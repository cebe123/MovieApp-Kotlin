package com.example.movie.di

import com.example.movie.model.Posts
import retrofit2.Response
import retrofit2.http.GET

interface SimpleApi {
    @GET("trending/movie/day?api_key=c82fef4dc3aba619d211c8f365739545")
    suspend fun fetchMovies(): Response<Posts>
}