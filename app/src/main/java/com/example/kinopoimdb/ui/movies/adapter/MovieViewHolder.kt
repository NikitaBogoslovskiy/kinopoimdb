package com.example.kinopoimdb.ui.movies.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.R

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.exact_movie_title)
    var id: Long = 0

    init {
        view.setOnClickListener {

        }
    }
}