package com.example.movie.repo

import com.example.movie.model.Posts
import com.example.movie.model.RetrofitInstance
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


class Repository @Inject constructor() {

    suspend fun getPosts(): Posts {
        try {
            val response = RetrofitInstance.api.fetchMovies()
            if (response.isSuccessful) {
                return response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                when (response.code()) {
                    400 -> throw IOException("Bad Request")
                    401 -> throw IOException("Unauthorized")
                    403 -> throw IOException("Forbidden")
                    404 -> throw IOException("Not Found")
                    else -> throw IOException("HTTP Error: ${response.code()}")
                }
            }
        } catch (e: IOException) {
            throw IOException("Network Error: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("An unexpected error occurred: ${e.message}", e)
        }
    }
}