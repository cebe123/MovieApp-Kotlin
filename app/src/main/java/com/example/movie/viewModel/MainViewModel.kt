package com.example.movie.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.WeatherResponse
import com.example.movie.repo.Repository
import com.example.movie.roomdb.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for fetching and managing movie posts.
 * This ViewModel uses Hilt for dependency injection and coroutines for asynchronous operations.
 *
 * It fetches movie data from the repository and stores it in the database.
 * It also provides LiveData for observing the fetched movies and any errors that occur.
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // Veritabanındaki filmler doğrudan gözlemleniyor
    val moviesFromDb: LiveData<List<Movies>> = repository.getMoviesFromDatabase()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _weatherData = MutableLiveData<Pair<String, Double>>()
    val weatherData: LiveData<Pair<String, Double>> get() = _weatherData


    fun fetchWeather() {
        viewModelScope.launch {
            try {
                val data = repository.getWeather()
                _weatherData.value = data // Update LiveData with city name and temperature
            } catch (e: Exception) {
                // Handle errors, possibly update an error LiveData
            }
        }
    }


    fun onButtonClick() {
        viewModelScope.launch {
            try {
                fetchWeather()
                repository.clearDatabase()
                // API üzerinden verileri çek ve veritabanına kaydet
                repository.getPostsFromApi()
                moviesFromDb

            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    suspend fun clearDatabase() {
        repository.clearDatabase()
    }
}
