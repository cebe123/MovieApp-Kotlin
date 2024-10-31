package com.example.movie.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "MOVIES"
)
data class Movies(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id") val movieId: Int = 0,
    @ColumnInfo(name = "title") val name: String,
)