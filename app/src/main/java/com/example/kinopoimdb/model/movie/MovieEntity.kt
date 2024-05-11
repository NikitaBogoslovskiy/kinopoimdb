package com.example.kinopoimdb.model.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "budget")
    val budget: String,

    @ColumnInfo(name = "homepage")
    val homepage: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "popularity")
    val popularity: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "revenue")
    val revenue: String,

    @ColumnInfo(name = "runtime")
    val runtime: String,

    @ColumnInfo(name = "movie_status")
    val movieStatus: String,

    @ColumnInfo(name = "tagline")
    val tagline: String,

    @ColumnInfo(name = "vote_average")
    val voteAverage: String,

    @ColumnInfo(name = "vote_count")
    val voteCount: String
)