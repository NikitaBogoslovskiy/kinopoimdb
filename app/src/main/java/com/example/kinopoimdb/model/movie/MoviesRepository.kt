package com.example.kinopoimdb.model.movie

import android.content.Context
import androidx.databinding.ObservableField
import com.example.kinopoimdb.model.api.AppApi
import com.example.kinopoimdb.model.api.AppApiServices
import com.example.kinopoimdb.model.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MoviesRepository(context: Context) {
    private val movieDao = AppDatabase.getDatabase(context).getMovieDao()
    private val characterDao = AppDatabase.getDatabase(context).getCharacterDao()
    private val appApiService: AppApiServices
        get() = AppApi.getClient().create(AppApiServices::class.java)

    var movies = emptyList<Movie>().toMutableList()
    var movieDetails = emptyList<MovieDetail>().toMutableList()
    private var movieToSave = ObservableField<Movie>()
    private var portionSize = 75

    init {
        movieToSave.addOnPropertyChangedCallback(object : androidx.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: androidx.databinding.Observable?, propertyId: Int) {
                val entities = movieToSave.get()?.toEntities()
                if (entities != null) {
                    movieDao.insertNew(entities.first)
                    if (entities.second != null)
                        characterDao.insertNew(entities.second as List<CharacterEntity>)
                }
            }
        })
    }

    fun findMovies(disableProgressBarCallback: () -> Unit,
                   updateMoviesListCallback: () -> Unit,
                   errorMessageCallback: (String) -> Unit) {
        appApiService.getMovies(portionSize, 0).enqueue(object : Callback<MutableList<MovieApi>> {
            override fun onFailure(call: Call<MutableList<MovieApi>>, t: Throwable) {
                disableProgressBarCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<MutableList<MovieApi>>, response: Response<MutableList<MovieApi>>) {
                movies.clear()
                for(movieApi in response.body() as MutableList<MovieApi>)
                    movies.add(Movie.fromApi(movieApi))
                disableProgressBarCallback.invoke()
                updateMoviesListCallback.invoke()
            }
        })
    }

    fun appendMovies(updateMoviesListCallback: () -> Unit,
                     errorMessageCallback: (String) -> Unit) {
        appApiService.getMovies(portionSize, movies.size).enqueue(object : Callback<MutableList<MovieApi>> {
            override fun onFailure(call: Call<MutableList<MovieApi>>, t: Throwable) {
                movies.clear()
                updateMoviesListCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<MutableList<MovieApi>>, response: Response<MutableList<MovieApi>>) {
                for(movieApi in response.body() as MutableList<MovieApi>)
                    movies.add(Movie.fromApi(movieApi))
                updateMoviesListCallback.invoke()
            }
        })
    }

    suspend fun findMovie(id: Long,
                  setTitleCallback: (String?) -> Unit,
                  disableProgressBarCallback: () -> Unit,
                  updateMovieDetailsCallback: () -> Unit,
                  errorMessageCallback: (String) -> Unit) {
        val movieEntity = movieDao.getById(id)
        if (movieEntity == null) {
            val movie = findMovieWithApi(
                id,
                setTitleCallback,
                disableProgressBarCallback,
                updateMovieDetailsCallback,
                errorMessageCallback
            )
            val entities = movie.toEntities()
            movieDao.insertNew(entities.first)
            if (entities.second != null)
                characterDao.insertNew(entities.second as List<CharacterEntity>)
        } else {
            findMovieWithDb(
                movieEntity,
                setTitleCallback,
                disableProgressBarCallback,
                updateMovieDetailsCallback
            )
        }
    }

    private suspend fun findMovieWithApi(id: Long,
                                 setTitleCallback: (String?) -> Unit,
                                 disableProgressBarCallback: () -> Unit,
                                 updateMovieDetailsCallback: () -> Unit,
                                 errorMessageCallback: (String) -> Unit): Movie {
        val call = appApiService.getMovie(AppApi.getToken(), id)
        return suspendCoroutine { cont ->
            call.enqueue(object : Callback<MovieApi> {
                override fun onFailure(call: Call<MovieApi>, t: Throwable) {
                    disableProgressBarCallback.invoke()
                    errorMessageCallback.invoke("Oops... Connection failed")
                }

                override fun onResponse(call: Call<MovieApi>, response: Response<MovieApi>) {
                    movieDetails.clear()
                    val movie = Movie.fromApi(response.body() as MovieApi)
                    movie.toMovieDetails().map {
                        if (it.key == "Title")
                            setTitleCallback.invoke(it.value)
                        else
                            movieDetails.add(MovieDetail(it.key, it.value))
                    }
                    disableProgressBarCallback.invoke()
                    updateMovieDetailsCallback.invoke()
                    cont.resume(movie)
                }
            })
        }
    }

    private fun findMovieWithDb(movieEntity: MovieEntity,
                                setTitleCallback: (String?) -> Unit,
                                disableProgressBarCallback: () -> Unit,
                                updateMovieDetailsCallback: () -> Unit) {
        val characterEntities = characterDao.getById(movieEntity.id)
        val movie = Movie.fromEntities(movieEntity, characterEntities)
        movie.toMovieDetails().map {
            if (it.key == "Title")
                setTitleCallback.invoke(it.value)
            else
                movieDetails.add(MovieDetail(it.key, it.value))
        }
        disableProgressBarCallback.invoke()
        updateMovieDetailsCallback.invoke()
    }

    companion object {
        @Volatile
        private var instance: MoviesRepository? = null
        fun getRepository(context: Context): MoviesRepository {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val newInstance = MoviesRepository(context)
                instance = newInstance
                return newInstance
            }
        }
    }

/*    suspend fun load() {
        itemsFlow.collect {
            items.clear()
            it.map { entity -> items.add(Item.fromEntity(entity)) }
            overallItems.set(items.size)
            checkedItems.set(items.filter { item -> item.isDone }.size)
        }
    }

    fun get(id: Long): Item? {
        return items.find { it.id == id }
    }

    fun insert(item: Item) {
        itemDao.insertNew(item.toEntity())
    }

    fun delete(itemId: Long) {
        itemDao.deleteById(itemId)
    }

    fun changeStatus(itemId: Long, newStatus: Boolean) {
        itemDao.changeStatusById(itemId, newStatus)
    }*/
}