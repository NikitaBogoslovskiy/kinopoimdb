package com.example.kinopoimdb.model.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "movie_id")
    val movieId: Long,

    @ColumnInfo(name = "actor_name")
    val actorName: String,

    @ColumnInfo(name = "character_name")
    val characterName: String,
)