package com.example.kinopoimdb.model.api

import com.example.kinopoimdb.model.movie.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

object AppApi {
    private var retrofit: Retrofit? = null
    private var baseUrl = "http://90.156.216.127/api/"
    private var token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJVc2VyIiwiZXhwIjoxNzE2MDU5MzA5LCJpc3MiOiJBdXRoU2VydmVyIiwiYXVkIjoiQXV0aENsaWVudCJ9.mSYHlPOLmf3I6MFqXseRQHA4Jwn5lS6T-D3WgffE5U0"

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
    fun getMovies(): Call<MutableList<Movie>>
}