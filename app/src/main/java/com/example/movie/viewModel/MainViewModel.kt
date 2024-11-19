package com.example.movie.viewModel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.repo.Repository
import com.example.movie.roomdb.Movies
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * ViewModel for fetching and managing movie posts.
 * This ViewModel uses Hilt for dependency injection and coroutines for asynchronous operations.
 *
 * It fetches movie data from the repository and stores it in the database.
 * It also provides LiveData for observing the fetched movies and any errors that occur.
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    var sendIntent: Intent = Intent()
    val moviesFromDb: LiveData<List<Movies>> = repository.getMoviesFromDatabase()
    private val _error = MutableLiveData<String?>()
    //init file
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("time", MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    init {
        checkUpdateTime()
    }

    private fun checkUpdateTime() {
        System.setProperty("user.timezone", "Europe/Istanbul")
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val getLastTime = sharedPrefs.getString("date", null)
        val currentTime = LocalDateTime.now(ZoneId.of("Europe/Istanbul"))

        if (getLastTime != null) {
            val lastTime = LocalDateTime.parse(getLastTime, formatter)
            val duration = java.time.Duration.between(lastTime, currentTime)
            val minutesDifference = duration.toMinutes()

            if (minutesDifference >= 60 * 24) {
                onUpdateButtonClick()
                editor.putString("date", currentTime.format(formatter)).apply()
                Log.i("applog", "(Data updated)")
            } else {
                Log.i("applog", "Veriler guncel")
            }
        } else {
            editor.putString("date", currentTime.format(formatter)).apply()
            Log.i("applog", "(İlk tarih verildi)")
        }
    }

    fun onUpdateButtonClick() {
        viewModelScope.launch {
            try {
                repository.clearDatabase()
                repository.getPostsFromApi()
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun setIntent() {
        Log.i("applog",moviesFromDb.value.toString())
        sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
                moviesFromDb.value?.joinToString{it.name})
            type = "text/plain"
        }.setClassName("com.example.secondapp", "com.example.secondapp.MainActivity")
    }

}