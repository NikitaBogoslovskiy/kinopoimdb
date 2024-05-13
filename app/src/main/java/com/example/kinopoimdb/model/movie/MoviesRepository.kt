package com.example.kinopoimdb.model.movie

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.databinding.ObservableField
import com.example.kinopoimdb.Dependencies
import com.example.kinopoimdb.model.api.AppApi
import com.example.kinopoimdb.model.api.AppApiServices
import com.example.kinopoimdb.model.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.getContext
import java.sql.Time
import java.util.Date
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


enum class ApiCallStatus {
    TokenExpired, ConnectionFailed, Success
}
data class ApiCallResult(var status: ApiCallStatus, var movie: Movie?)

class MoviesRepository(context: Context) {
    private val movieDao = AppDatabase.getDatabase(context).getMovieDao()
    private val characterDao = AppDatabase.getDatabase(context).getCharacterDao()
    private val appApiService: AppApiServices
        get() = AppApi.getClient().create(AppApiServices::class.java)
    private var portionSize = 75

    var movies = emptyList<Movie>().toMutableList()
    var movieDetails = emptyList<MovieDetail>().toMutableList()
    var expirationPeriod: Long = 86400000
    var cacheCheckFrequency: Long = 600000
    var currentSearch: String = ""
    var canLoadMore = true

    fun findMovies(disableProgressBarCallback: () -> Unit,
                   updateMoviesListCallback: () -> Unit,
                   errorMessageCallback: (String) -> Unit) {
        appApiService.getMovies(currentSearch, portionSize, 0).enqueue(object : Callback<MutableList<MovieApi>> {
            override fun onFailure(call: Call<MutableList<MovieApi>>, t: Throwable) {
                disableProgressBarCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<MutableList<MovieApi>>, response: Response<MutableList<MovieApi>>) {
                movies.clear()
                val movieApis = response.body() as MutableList<MovieApi>
                canLoadMore = movieApis.size == portionSize
                for(movieApi in movieApis)
                    movies.add(Movie.fromApi(movieApi))
                disableProgressBarCallback.invoke()
                updateMoviesListCallback.invoke()
            }
        })
    }

    fun appendMovies(disableLoadMoreProgressCallback: () -> Unit,
                     updateMoviesListCallback: () -> Unit,
                     errorMessageCallback: (String) -> Unit) {
        appApiService.getMovies(currentSearch, portionSize, movies.size).enqueue(object : Callback<MutableList<MovieApi>> {
            override fun onFailure(call: Call<MutableList<MovieApi>>, t: Throwable) {
                movies.clear()
                disableLoadMoreProgressCallback.invoke()
                updateMoviesListCallback.invoke()
                errorMessageCallback.invoke("Oops... Connection failed")
            }
            override fun onResponse(call: Call<MutableList<MovieApi>>, response: Response<MutableList<MovieApi>>) {
                val movieApis = response.body() as MutableList<MovieApi>
                canLoadMore = movieApis.size == portionSize
                for(movieApi in movieApis)
                    movies.add(Movie.fromApi(movieApi))
                disableLoadMoreProgressCallback.invoke()
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
            if (movie != null) {
                val entities = movie.toEntities()
                movieDao.insertNew(entities.first)
                if (entities.second != null)
                    characterDao.insertNew(entities.second as List<CharacterEntity>)
            }
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
                                 errorMessageCallback: (String) -> Unit): Movie? {
        val call = appApiService.getMovie(AppApi.getToken() ?: "", id)
        val result = runApiCall(
            call,
            setTitleCallback,
            disableProgressBarCallback,
            updateMovieDetailsCallback,
            errorMessageCallback
        )
        when(result.status) {
            ApiCallStatus.TokenExpired -> {
                updateToken()
                return runApiCall(
                    appApiService.getMovie(AppApi.getToken() ?: "", id),
                    setTitleCallback,
                    disableProgressBarCallback,
                    updateMovieDetailsCallback,
                    errorMessageCallback
                ).movie
            }
            else -> {
                return result.movie
            }
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

    fun removeOldCache() {
        val ids = movieDao.getExpiredIds(Date())
        movieDao.deleteByIds(ids)
        characterDao.deleteByIds(ids)
    }

    fun tryRegisterCredentials() {
        val credentials = AppApi.getCredentials()
        if (credentials.login != null && credentials.password != null)
            return
        credentials.login = Dependencies.androidId + "-" + UUID.randomUUID().toString()
        credentials.password = UUID.randomUUID().toString()
        val call = appApiService.register(credentials)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                AppApi.setCredentials(credentials)
            }
        })
    }

    private suspend fun updateToken(): Boolean {
        val call = appApiService.auth(AppApi.getCredentials())
        return suspendCoroutine { cont ->
            call.enqueue(object : Callback<TokenApi> {
                override fun onFailure(call: Call<TokenApi>, t: Throwable) {
                    cont.resume(false)
                }
                override fun onResponse(call: Call<TokenApi>, response: Response<TokenApi>) {
                    Log.i("Info", "Booba")
                    AppApi.setToken((response.body() as TokenApi).content ?: "")
                    cont.resume(true)
                }
            })
        }
    }

    private suspend fun runApiCall(call: Call<MovieApi>,
                                   setTitleCallback: (String?) -> Unit,
                                   disableProgressBarCallback: () -> Unit,
                                   updateMovieDetailsCallback: () -> Unit,
                                   errorMessageCallback: (String) -> Unit): ApiCallResult {
        return suspendCoroutine { cont ->
            call.enqueue(object : Callback<MovieApi> {
                override fun onFailure(call: Call<MovieApi>, t: Throwable) {
                    disableProgressBarCallback.invoke()
                    errorMessageCallback.invoke("Oops... Connection failed")
                    cont.resume(ApiCallResult(ApiCallStatus.ConnectionFailed, null))
                }

                override fun onResponse(call: Call<MovieApi>, response: Response<MovieApi>) {
                    if (response.code() == 401) {
                        cont.resume(ApiCallResult(ApiCallStatus.TokenExpired, null))
                        return
                    }
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
                    cont.resume(ApiCallResult(ApiCallStatus.Success, movie))
                }
            })
        }
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
}