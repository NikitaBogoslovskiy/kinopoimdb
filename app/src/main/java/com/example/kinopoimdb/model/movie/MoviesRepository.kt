package com.example.kinopoimdb.model.movie

import android.content.Context
import android.database.Observable
import androidx.databinding.ObservableField
import com.example.kinopoimdb.model.api.AppApi
import com.example.kinopoimdb.model.api.AppApiServices
import com.example.kinopoimdb.model.database.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(context: Context) {
    private val movieDao = AppDatabase.getDatabase(context).getMovieDao()
    private val appApiService: AppApiServices
        get() = AppApi.getClient().create(AppApiServices::class.java)

    var movies = emptyList<Movie>().toMutableList()
    var movieDetails = emptyList<MovieDetail>().toMutableList()
    private var portionSize = 75

    fun findMovies(disableProgressBarCallback: () -> Unit,
                   updateMoviesListCallback: () -> Unit,
                   errorMessageCallback: (String) -> Unit) {
        appApiService.getMovies(portionSize, 0).enqueue(object : Callback<MutableList<Movie>> {
            override fun onFailure(call: Call<MutableList<Movie>>, t: Throwable) {
                disableProgressBarCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<MutableList<Movie>>, response: Response<MutableList<Movie>>) {
                movies.clear()
                for(movie in response.body() as MutableList<Movie>)
                    movies.add(movie)
                disableProgressBarCallback.invoke()
                updateMoviesListCallback.invoke()
            }
        })
    }

    fun appendMovies(updateMoviesListCallback: () -> Unit,
                     errorMessageCallback: (String) -> Unit) {
        appApiService.getMovies(portionSize, movies.size).enqueue(object : Callback<MutableList<Movie>> {
            override fun onFailure(call: Call<MutableList<Movie>>, t: Throwable) {
                movies.clear()
                updateMoviesListCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<MutableList<Movie>>, response: Response<MutableList<Movie>>) {
                for(movie in response.body() as MutableList<Movie>)
                    movies.add(movie)
                updateMoviesListCallback.invoke()
            }
        })
    }

    fun findMovie(id: Long,
                  setTitleCallback: (String?) -> Unit,
                  disableProgressBarCallback: () -> Unit,
                  updateMovieDetailsCallback: () -> Unit,
                  errorMessageCallback: (String) -> Unit) {
        appApiService.getMovie(AppApi.getToken(), id).enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                disableProgressBarCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                movieDetails.clear()
                (response.body() as Movie).toMovieDetailsCollection().map {
                    if (it.key == "Title")
                        setTitleCallback.invoke(it.value)
                    else
                        movieDetails.add(MovieDetail(it.key, it.value))
                }
                disableProgressBarCallback.invoke()
                updateMovieDetailsCallback.invoke()
            }
        })
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