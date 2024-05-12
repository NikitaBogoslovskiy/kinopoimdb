package com.example.kinopoimdb.ui.movies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.Dependencies
import com.example.kinopoimdb.R
import com.example.kinopoimdb.model.movie.Movie


class MoviesViewAdapter(private var movies: MutableList<Movie>) :
    RecyclerView.Adapter<MovieViewHolder>() {

    lateinit var errorMessageCallback: (String) -> Unit

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_item, viewGroup, false)
        val viewHolder = MovieViewHolder(view)
        return viewHolder
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
        viewHolder.id = movies[position].id
        viewHolder.text.text = movies[position].title
        if (position == (movies.size - 1) && Dependencies.moviesRepository.canLoadMore) {
            Dependencies.moviesRepository.appendMovies(
                updateMoviesListCallback = { notifyDataSetChanged() },
                errorMessageCallback = errorMessageCallback
            )

        }
    }

    override fun getItemCount() = movies.size
}