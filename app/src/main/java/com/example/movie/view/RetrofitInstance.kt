package com.example.movie.view

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* Uygulama başında bir kere çalıştırılır.
* Singleton bir yapıdır.
* lazy---->Uygulama ilk kullanılacağı anda create edilir
* */


object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }


}