package com.example.kinopoimdb.ui.movies

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kinopoimdb.Dependencies
import com.example.kinopoimdb.model.movie.MoviesRepository
import com.example.kinopoimdb.ui.movies.adapter.MoviesViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel : ViewModel() {
    var moviesViewAdapter: MoviesViewAdapter

    init {
        val movies = mutableListOf("Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3",
            "Movie1", "Movie2", "Movie3")
        moviesViewAdapter = MoviesViewAdapter(Dependencies.moviesRepository.movies)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun findMovies(activateProgressBarCallback: () -> Unit,
                   disableProgressBarCallback: () -> Unit,
                   errorMessageCallback: (String) -> Unit) {
        Dependencies.moviesRepository.movies.clear()
        moviesViewAdapter.notifyDataSetChanged()
        activateProgressBarCallback.invoke()
        Dependencies.moviesRepository.findMovies(
            disableProgressBarCallback = disableProgressBarCallback,
            updateMoviesListCallback = {
                moviesViewAdapter.notifyDataSetChanged()
            },
            errorMessageCallback = errorMessageCallback
        )
    }
}