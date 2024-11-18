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
    private val todayString = LocalDateTime.now().toString()

    //init file
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("time", MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    init {
        checkUpdateTime()
    }

    private fun checkUpdateTime() {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        Log.i("applog", "formatter: $formatter")
        val getLastTime = sharedPrefs.getString("date", null)
        Log.i("applog", "todayString: $todayString")
        Log.i("applog", "getLastTime: $getLastTime")
        if (getLastTime != null) {
            val lastTime = LocalDateTime.parse(getLastTime, formatter)
            val currentTime: LocalDateTime = LocalDateTime.parse(todayString, formatter)
            val duration = java.time.Duration.between(lastTime, currentTime)
            val minutesDifference = duration.toMinutes()

            if (minutesDifference >= 1) {
                onUpdateButtonClick()
                editor.remove("date")
                editor.putString("date", todayString).apply()
                Log.i("applog", "(Data updated)")

            } else {
                Log.i("applog", "Veriler guncel")
            }
        } else {
            editor.putString("date", todayString).apply()
            Log.i("applog", "(ilk tarih verildi)")
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
        sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, moviesFromDb.value.toString())
            type = "text/plain"
        }.setClassName("com.example.secondapp", "com.example.secondapp.MainActivity")
    }
}
