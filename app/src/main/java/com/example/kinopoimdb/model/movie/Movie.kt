package com.example.kinopoimdb.model.movie

data class Movie(
    val id: Long,
    val title: String? = null,
    val budget: String? = null,
    val homepage: String? = null,
    val overview: String? = null,
    val popularity: String? = null,
    val releaseDate: String? = null,
    val revenue: String? = null,
    val runtime: String? = null,
    val movieStatus: String? = null,
    val tagline: String? = null,
    val voteAverage: String? = null,
    val voteCount: String? = null
)