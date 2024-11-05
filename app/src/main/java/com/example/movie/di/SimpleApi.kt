package com.example.movie.di

import com.example.movie.model.Posts
import com.example.movie.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpleApi {
    @GET("trending/movie/day?api_key=c82fef4dc3aba619d211c8f365739545")
    suspend fun fetchMovies(): Response<Posts>
}

interface WeatherApi {
    @GET("weather")
    suspend fun fetchWeather(
        @Query("q") city: String = "Istanbul", // İstanbul'u sabit olarak ayarlayabiliriz.
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // Celsius olarak sonuç almak için "metric" ekliyoruz
    ): Response<WeatherResponse> // WeatherResponse yapısına dönmeli
}
