package com.example.kinopoimdb.ui.movie.adapter

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.Dependencies
import com.example.kinopoimdb.R
import com.example.kinopoimdb.model.movie.Movie
import com.example.kinopoimdb.model.movie.MovieDetail


class MovieDetailsViewAdapter(private var movieDetails: MutableList<MovieDetail>) :
    RecyclerView.Adapter<MovieDetailViewHolder>() {

    lateinit var errorMessageCallback: (String) -> Unit

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_detail, viewGroup, false)
        val viewHolder = MovieDetailViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: MovieDetailViewHolder, position: Int) {
        viewHolder.key.text = movieDetails[position].key
        viewHolder.value.text = movieDetails[position].value
        if (movieDetails[position].key == "Actors") {
            val params = viewHolder.keyLayout.layoutParams as ConstraintLayout.LayoutParams
            params.verticalBias = 0f
            viewHolder.keyLayout.layoutParams = params
        }
        else {
            val params = viewHolder.keyLayout.layoutParams as ConstraintLayout.LayoutParams
            params.verticalBias = 0.5f
            viewHolder.keyLayout.layoutParams = params
        }
    }

    override fun getItemCount() = movieDetails.size
}