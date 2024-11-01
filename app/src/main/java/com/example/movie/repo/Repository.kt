package com.example.movie.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.movie.di.SimpleApi
import com.example.movie.model.Posts
import com.example.movie.roomdb.Movies
import com.example.movie.roomdb.MoviesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: SimpleApi,
    private val moviesDao: MoviesDao
) {

    suspend fun getPostsFromApi(): Posts {
        try {
            // Make the network request to fetch movies
            val response = api.fetchMovies()
            // Check if the response was successful
            if (response.isSuccessful) {
                //clearDatabase()
                try {
                    val moviesToInsert = response.body()?.results?.map { Movies(name = it.title) }
                    moviesToInsert?.let {
                        moviesDao.insertAll(it)
                        Log.d("Repository", "Inserted movies: $it")
                    }
                } catch (e: Exception) {
                    throw e
                }
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

    fun getMoviesFromDatabase(): LiveData<List<Movies>> {
        return moviesDao.readAll()
    }

}