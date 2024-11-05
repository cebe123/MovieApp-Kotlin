//package com.example.movie.di
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import android.util.Log
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//
//
//class WeatherService @Inject constructor() : Service() {
//
//    private val serviceScope = CoroutineScope(Dispatchers.IO)
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        log("Service Initialized")
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        log("Service Started")
//        serviceScope.launch {
//            fetchAndPostData()
//        }
//        return START_STICKY // If the service is killed, it will be automatically restarted
//    }
//
//    private suspend fun fetchAndPostData() {
//        // Simulate network request
//        withContext(Dispatchers.IO) {
//            delay(1000)
//
//            log("Data fetched and posted")
//        }
//    }
//}
//
//private fun log(str: String) {
//    Log.d("TAG", "log: $str")
//}
