package com.example.kinopoimdb.model.movie

data class MoviePersonApi (
    val movieId: Long,
    val personId: Long,
    val characterName: String
)
data class PersonApi (
    val id: Long,
    val name: String,
    val movies: List<Any>,
    val moviePersons: List<MoviePersonApi>
)

data class MovieApi(
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
    val voteCount: String? = null,
    val persons: List<PersonApi>? = null)