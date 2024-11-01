package com.example.movie.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoviesDao {

    @Query("SELECT * FROM Movies_Table")
    fun readAll(): LiveData<List<Movies>>

    @Insert
    suspend fun insertAll(movies: List<Movies>)

    @Query("DELETE FROM Movies_Table")
    suspend fun deleteAll()
}