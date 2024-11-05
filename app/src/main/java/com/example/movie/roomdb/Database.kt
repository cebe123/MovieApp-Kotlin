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
    entities = [Movies::class], // The data classes to be stored in the database
    version = 1, // The database version (increment when schema changes)
    exportSchema = false // Whether to export the schema (usually false for production)
)
abstract class MoviesDB : RoomDatabase() {

    /**
     * Provides access to the MoviesDao, which defines the database operations
     * for the Movies entity.
     */
    abstract fun moviesDao(): MoviesDao

    companion object {
        /**
         * A volatile variable to hold the database instance.
         * This ensures that the instance is always up-to-date and visible to all threads.
         */
        @Volatile
        private var INSTANCE: MoviesDB? = null

        /**
         * Gets the database instance. If it doesn't exist, it creates a new one.
         * This ensures that only one instance of the database is created.
         */
        fun getDatabase(context: Context): MoviesDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(lock = this) { // Synchronized block to prevent multiple instances
                val instance = Room.databaseBuilder(
                    context.applicationContext, // The application context
                    MoviesDB::class.java, // The database class
                    "movies_database" // The database name
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

/**
 * This Hilt module provides dependencies related to the Room database.
 * It provides the database instance and the MoviesDao.
 */
@Module
@InstallIn(SingletonComponent::class) // Installs this module in the SingletonComponent
object DatabaseModule {

    /**
     * Provides the MoviesDB instance.
     * This function is annotated with @Provides and @Singleton,
     * indicating that it provides a singleton instance of the database.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoviesDB {
        return Room.databaseBuilder(
            context,
            MoviesDB::class.java,
            "movies_database"
        ).build()
    }

    /**
     * Provides the MoviesDao instance.
     * This function depends on the MoviesDB instance and returns its MoviesDao.
     */
    @Provides
    fun provideMoviesDao(database: MoviesDB): MoviesDao {
        return database.moviesDao()
    }
}