package com.example.kinopoimdb.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.kinopoimdb.model.movie.CharacterDao
import com.example.kinopoimdb.model.movie.CharacterEntity
import com.example.kinopoimdb.model.movie.MovieDao
import com.example.kinopoimdb.model.movie.MovieEntity
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    version = 1,
    entities = [
        MovieEntity::class,
        CharacterEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getCharacterDao(): CharacterDao

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