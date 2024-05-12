package com.example.kinopoimdb.ui.movies.adapter

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.R

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.exact_movie_title)
    var id: Long = 0

    init {
        view.setOnClickListener {
            val args = Bundle()
            args.putLong("id", id)
            it.findNavController().navigate(R.id.action_navigation_movies_to_movieFragment, args)
        }
    }
}