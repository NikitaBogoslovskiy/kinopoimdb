package com.example.kinopoimdb.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.R
import com.example.kinopoimdb.model.movie.Movie


class MoviesViewAdapter(private var movies: MutableList<Movie>) :
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_item, viewGroup, false)
        val viewHolder = MovieViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
        viewHolder.text.text = movies[position].title
    }

    override fun getItemCount() = movies.count()
}