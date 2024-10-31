package com.example.movie.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoviesDao {

    @Query("select * from MOVIES")
    fun getAll() : List<Movies>

    @Query("select * from MOVIES where title")
    fun getById(id: Int) : Movies

    @Insert
    fun insert(title: Movies)

    @Delete
    fun delete(title: Movies)

    @Update
    fun update(title: Movies)

}