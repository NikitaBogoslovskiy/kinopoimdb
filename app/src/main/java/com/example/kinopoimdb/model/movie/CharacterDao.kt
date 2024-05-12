package com.example.kinopoimdb.model.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(entity = CharacterEntity::class)
    fun insertNew(characters: List<CharacterEntity>)

    @Query("select t.id, " +
            "t.movie_id, " +
            "t.actor_name, " +
            "t.character_name " +
            "from characters t " +
            "where t.movie_id = :movieId;")
    fun getById(movieId: Long): List<CharacterEntity>

    @Query("delete from characters " +
            "where movie_id in (:movieIds);")
    fun deleteByIds(movieIds: List<Long>)

/*    @Query("delete from movies " +
            "where id = :movieId;")
    fun deleteById(movieId: Long)*/
}