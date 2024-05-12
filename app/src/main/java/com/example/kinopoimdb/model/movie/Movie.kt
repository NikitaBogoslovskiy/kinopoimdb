package com.example.kinopoimdb.model.movie

import com.example.kinopoimdb.Dependencies
import java.util.Date

data class MovieDetail(val key: String, val value: String)

class Character(
    val actorName: String,
    val characterName: String
)

class Movie(
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
    val characters: List<Character>? = null
) {
    fun toMovieDetails(): MutableList<MovieDetail> {
        val collection = emptyList<MovieDetail>().toMutableList()
        if (!title.isNullOrEmpty())
            collection.add(MovieDetail("Title", title))
        if (!voteAverage.isNullOrEmpty())
            collection.add(MovieDetail("Rating", voteAverage))
        if (!voteCount.isNullOrEmpty())
            collection.add(MovieDetail("Votes Number", voteCount))
        if (!movieStatus.isNullOrEmpty())
            collection.add(MovieDetail("Status", movieStatus))
        if (!releaseDate.isNullOrEmpty())
            collection.add(MovieDetail("Release Date", releaseDate))
        if (!budget.isNullOrEmpty())
            collection.add(MovieDetail("Budget", budget))
        if (!popularity.isNullOrEmpty())
            collection.add(MovieDetail("Popularity", popularity))
        if (!tagline.isNullOrEmpty())
            collection.add(MovieDetail("Tagline", tagline))
        if (!overview.isNullOrEmpty())
            collection.add(MovieDetail("Overview", overview))
        if (!homepage.isNullOrEmpty())
            collection.add(MovieDetail("Home Page", homepage))
        if (!characters.isNullOrEmpty())
            collection.add(MovieDetail("Actors",
                characters.joinToString("\n\n") { "${it.actorName}\n(${it.characterName})" }))
        return collection
    }

    fun toEntities() = Pair(
        MovieEntity(
            id = id,
            title = title ?: "",
            budget = budget ?: "",
            homepage = homepage ?: "",
            overview = overview ?: "",
            popularity = popularity ?: "",
            releaseDate = releaseDate ?: "",
            revenue = revenue ?: "",
            runtime = runtime ?: "",
            movieStatus = movieStatus ?: "",
            tagline = tagline ?: "",
            voteAverage = voteAverage ?: "",
            voteCount = voteCount ?: "",
            expiringAt = Date(Date().time + Dependencies.moviesRepository.expirationPeriod)
        ),
        characters?.map { CharacterEntity(0, id, it.actorName, it.characterName) }
    )

    companion object {
        @JvmStatic
        fun fromApi(apiObject: MovieApi) = Movie(
                id = apiObject.id,
                title = apiObject.title,
                budget = apiObject.budget,
                homepage = apiObject.homepage,
                overview = apiObject.overview,
                popularity = apiObject.popularity,
                releaseDate = apiObject.releaseDate,
                revenue = apiObject.revenue,
                runtime = apiObject.runtime,
                movieStatus = apiObject.movieStatus,
                tagline = apiObject.tagline,
                voteAverage = apiObject.voteAverage,
                voteCount = apiObject.voteCount,
                characters = apiObject.persons?.map { Character(it.name, it.moviePersons.first().characterName) }
        )

        @JvmStatic
        fun fromEntities(movie: MovieEntity, characters: List<CharacterEntity>?) = Movie(
                id = movie.id,
                title = movie.title,
                budget = movie.budget,
                homepage = movie.homepage,
                overview = movie.overview,
                popularity = movie.popularity,
                releaseDate = movie.releaseDate,
                revenue = movie.revenue,
                runtime = movie.runtime,
                movieStatus = movie.movieStatus,
                tagline = movie.tagline,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount,
                characters = characters?.map { Character(it.actorName, it.characterName) }
        )
    }
}