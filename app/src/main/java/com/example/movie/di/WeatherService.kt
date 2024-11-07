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
class WeatherService : Service() {

    @Inject
    lateinit var repository: Repository

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private val _weatherData = MutableLiveData<Pair<String, Double>>()  // City and temperature
    val weatherData: LiveData<Pair<String, Double>> = _weatherData

    // Servise bağlanmak için kullanılan LocalBinder sınıfı
    inner class LocalBinder : Binder() {
        fun getService(): WeatherService = this@WeatherService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        log("Service Initialized")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            log("Service Started")
            fetchAndPostData() // Start fetching and posting data
        }
        return START_STICKY // If the service is killed, it will be automatically restarted
    }

    private suspend fun fetchAndPostData() {
        try {
            // Hava durumu verisini repository'den çekiyoruz
            val weatherDataService =
                repository.getWeather()
            log("Weather Data: $weatherDataService")

            // Veriyi LiveData'ya gönderiyoruz
            _weatherData.postValue(
                Pair(
                    weatherDataService.first,
                    weatherDataService.second
                )
            )  // Pair(city, temperature)

            delay(10000) // Repeat every 60 seconds
            fetchAndPostData() // Continue fetching the data
        } catch (e: Exception) {
            log("Error fetching data: ${e.message}")
        }
    }

    private fun log(str: String) {
        Log.d("TAG", "log: $str")
    }
}
