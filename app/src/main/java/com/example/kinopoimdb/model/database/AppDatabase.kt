package com.example.kinopoimdb.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kinopoimdb.model.movie.MovieDao
import com.example.kinopoimdb.model.movie.MovieEntity

@Database(
    version = 1,
    entities = [
        MovieEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movies_database"
                ).build()
                instance = newInstance
                return newInstance
            }
        }
    }
}