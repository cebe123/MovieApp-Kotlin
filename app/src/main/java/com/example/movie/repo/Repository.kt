package com.example.movie.repo

import com.example.movie.model.Posts
import com.example.movie.di.SimpleApi
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class for fetching movie posts from the network.
 * This class handles network requests and exceptions.
 */

class Repository @Inject constructor(private val api: SimpleApi) {

    /**
     * Fetches movie posts from the network.
     *
     * @return A [Posts] object containing the movie posts.
     */

    suspend fun getPosts(): Posts {
        try {
            // Make the network request to fetch movies
            val response = api.fetchMovies()
            // Check if the response was successful
            if (response.isSuccessful) {
                // Return the response body if it's not null, otherwise throw an exception
                return response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                // Handle different HTTP error codes
                when (response.code()) {
                    400 -> throw IOException("Bad Request")
                    401 -> throw IOException("Unauthorized")
                    403 -> throw IOException("Forbidden")
                    404 -> throw IOException("Not Found")
                    else -> throw IOException("HTTP Error: ${response.code()}")
                }
            }
        } catch (e: IOException) {
            // Wrap IOExceptions with a more descriptive message
            throw IOException("Network Error: ${e.message}", e)
        } catch (e: Exception) {
            // Wrap other exceptions with a generic message
            throw Exception("An unexpected error occurred: ${e.message}", e)
        }
    }
}