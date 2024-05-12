package com.example.kinopoimdb.model.movie

data class MovieDetail(val key: String, val value: String)

data class MoviePerson(
    val movieId: Long,
    val personId: Long,
    val characterName: String
)
data class Person(
    val id: Long,
    val name: String,
    val movies: List<Any>,
    val moviePersons: List<MoviePerson>
)

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
    val voteCount: String? = null,
    val persons: List<Person>? = null) {

    fun toMovieDetailsCollection(): MutableList<MovieDetail> {
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
        if (!persons.isNullOrEmpty())
            collection.add(MovieDetail("Actors",
                persons.joinToString("\n\n") { "${it.name}\n(${it.moviePersons.first().characterName})" }))
        return collection
    }
}