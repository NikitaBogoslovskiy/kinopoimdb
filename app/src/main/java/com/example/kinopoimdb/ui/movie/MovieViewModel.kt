package com.example.kinopoimdb.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.kinopoimdb.Dependencies
import com.example.kinopoimdb.ui.movie.adapter.MovieDetailsViewAdapter

class MovieViewModel : ViewModel() {
    var movieDetailsViewAdapter = MovieDetailsViewAdapter(Dependencies.moviesRepository.movieDetails)

    @SuppressLint("NotifyDataSetChanged")
    fun findMovie(id: Long,
                  setTitleCallback: (String?) -> Unit,
                  activateProgressBarCallback: () -> Unit,
                  disableProgressBarCallback: () -> Unit,
                  errorMessageCallback: (String) -> Unit) {
        Dependencies.moviesRepository.movieDetails.clear()
        movieDetailsViewAdapter.notifyDataSetChanged()
        activateProgressBarCallback.invoke()
        Dependencies.moviesRepository.findMovie(
            id = id,
            setTitleCallback = setTitleCallback,
            disableProgressBarCallback = disableProgressBarCallback,
            updateMovieDetailsCallback = {
                movieDetailsViewAdapter.notifyDataSetChanged()
            },
            errorMessageCallback = errorMessageCallback
        )
    }
}