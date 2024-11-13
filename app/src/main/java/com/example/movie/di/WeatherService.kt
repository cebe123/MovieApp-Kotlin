package com.example.movie.di

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movie.repo.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeatherService: Service() {

    @Inject
    lateinit var repository: Repository

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private val _weatherData = MutableLiveData<Pair<String, Double>>()  // City and temperature
    val weatherData: LiveData<Pair<String, Double>> = _weatherData

    // Servise bağlanmak için kullanılan LocalBinder sınıfı
    inner class LocalBinder : Binder() {
        fun getService(): WeatherService = this@WeatherService
    }

    override fun onBind(intent: Intent?): IBinder {
        val binder = LocalBinder()
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        log("Service Initialized")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            log("Service Started")
            fetchAndPostData()
        }
        return START_STICKY // If the service is killed, it will be automatically restarted
    }

    private suspend fun fetchAndPostData() {
        while (true) {
            try {
                val weatherDataService = repository.getWeather()
                log("Weather Data: $weatherDataService")

                // Post the received data to LiveData
                _weatherData.postValue(
                    Pair(weatherDataService.first, weatherDataService.second)
                )
                delay(60000)
            } catch (e: Exception) {
                _weatherData.postValue(
                    Pair("Error", 0.0)
                )
                log("Error fetching data: ${e.message}")
                delay(3000)
            }
        }
    }

    private fun log(str: String) {
        Log.d("TAG", "log: $str")
    }
}
