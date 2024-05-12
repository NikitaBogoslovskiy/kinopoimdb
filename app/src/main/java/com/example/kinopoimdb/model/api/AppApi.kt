package com.example.kinopoimdb.model.api

import com.example.kinopoimdb.model.movie.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

object AppApi {
    private var retrofit: Retrofit? = null
    private var baseUrl = "http://90.156.216.127/api/"
    private var token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJVc2VyIiwiZXhwIjoxNzE2MDc3MTAzLCJpc3MiOiJBdXRoU2VydmVyIiwiYXVkIjoiQXV0aENsaWVudCJ9.WTn6i9KHoEszfzywbfm71omGvuBEAbOaPZQEevH5D8I"

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getToken() = token
}

interface AppApiServices {
    @GET("movies")
    fun getMovies(@Query("count") count: Int, @Query("offset") offset: Int): Call<MutableList<Movie>>

    @GET("movies/{id}")
    fun getMovie(@Header("Authorization") token: String, @Path("id") id: Long): Call<Movie>
}