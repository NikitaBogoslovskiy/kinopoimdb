package com.example.kinopoimdb.model.api

import com.example.kinopoimdb.Dependencies
import com.example.kinopoimdb.model.movie.Movie
import com.example.kinopoimdb.model.movie.MovieApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class Credentials(var login: String?, var password: String?)

object AppApi {
    private var retrofit: Retrofit? = null
    private var baseUrl = "http://90.156.216.127/api/"
    private var token = "Bearer yJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJVc2VyIiwiZXhwIjoxNzE2MDc3MTAzLCJpc3MiOiJBdXRoU2VydmVyIiwiYXVkIjoiQXV0aENsaWVudCJ9.WTn6i9KHoEszfzywbfm71omGvuBEAbOaPZQEevH5D8I"

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getToken() = Dependencies.sharedPreferences.getString("token", null)

    fun setToken(token: String) {
        with(Dependencies.sharedPreferences.edit()) {
            putString("token", "Bearer $token")
            apply()
        }
    }

    fun getCredentials(): Credentials {
        val login = Dependencies.sharedPreferences.getString("login", null)
        val password = Dependencies.sharedPreferences.getString("password", null)
        return Credentials(login, password)
    }
}

interface AppApiServices {
    @GET("movies")
    fun getMovies(@Query("count") count: Int, @Query("offset") offset: Int): Call<MutableList<MovieApi>>

    @GET("movies/{id}")
    fun getMovie(@Header("Authorization") token: String, @Path("id") id: Long): Call<MovieApi>

    @POST("auth")
    fun auth(@Body credentials: Credentials)
}