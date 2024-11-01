package com.example.movie.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(
    entities = [
        Movies::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MoviesDB : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var INSTANCE: MoviesDB? = null

        fun getDatabase(context: Context): MoviesDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(lock = this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesDB::class.java,
                    "movies_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoviesDB {
        return Room.databaseBuilder(
            context,
            MoviesDB::class.java,
            "movies_database"
        ).build()
    }

    @Provides
    fun provideMoviesDao(database: MoviesDB): MoviesDao {
        return database.moviesDao()
    }
}