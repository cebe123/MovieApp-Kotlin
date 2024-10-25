package com.example.movie.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* Bu özellik ilk erişildiğinde oluşturulur ve daha sonraki kullanımlar
* için önbelleğe alınır.
* */


object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * [SimpleApi] arayüzünün örneği.
     *
     * Bu özellik, [SimpleApi] arayüzünde tanımlanan API yöntemlerine erişim
     * sağlar.
     */

    val api: SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }


}