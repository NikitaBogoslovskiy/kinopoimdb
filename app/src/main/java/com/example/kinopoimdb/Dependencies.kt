package com.example.kinopoimdb

import android.content.SharedPreferences
import com.example.kinopoimdb.model.movie.MoviesRepository

object Dependencies {
    lateinit var moviesRepository: MoviesRepository
    lateinit var sharedPreferences: SharedPreferences
    lateinit var androidId: String
}