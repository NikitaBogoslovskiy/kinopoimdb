package com.example.kinopoimdb.model.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(entity = MovieEntity::class)
    fun insertNew(movie: MovieEntity)

    @Query("select t.id, " +
            "t.title, " +
            "t.budget, " +
            "t.homepage, " +
            "t.overview, " +
            "t.popularity, " +
            "t.release_date, " +
            "t.revenue, " +
            "t.runtime, " +
            "t.movie_status, " +
            "t.tagline, " +
            "t.vote_average, " +
            "t.vote_count " +
            "from movies t;")
    fun getAll(): Flow<List<MovieEntity>>

    @Query("select t.id, " +
            "t.title, " +
            "t.budget, " +
            "t.homepage, " +
            "t.overview, " +
            "t.popularity, " +
            "t.release_date, " +
            "t.revenue, " +
            "t.runtime, " +
            "t.movie_status, " +
            "t.tagline, " +
            "t.vote_average, " +
            "t.vote_count " +
            "from movies t " +
            "where t.id = :movieId;")
    fun getById(movieId: Long): Flow<MovieEntity>

    @Query("delete from movies " +
            "where id = :movieId;")
    fun deleteById(movieId: Long)
}