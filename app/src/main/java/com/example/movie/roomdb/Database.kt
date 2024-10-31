package com.example.movie.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Movies::class
    ],
    version = 1
)
abstract class SchoolDB : RoomDatabase() {

    abstract val schoolDao: MoviesDao

    companion object {
        private var INSTANCE: SchoolDB? = null

        fun getInstance(context: Context): SchoolDB {
            return INSTANCE ?: Room.databaseBuilder(
                context,
                SchoolDB::class.java, // veritabanı sınıfı
                "school_db" // veritabanı adı
            ).allowMainThreadQueries() // main thread de kullanıma izin veriyoruz
                .build().also {
                    INSTANCE = it
                }
        }
    }

}