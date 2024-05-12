package com.example.kinopoimdb.ui.movie.adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.R

class MovieDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val key: TextView = view.findViewById(R.id.detail_key)
    val value: TextView = view.findViewById(R.id.detail_value)
    val keyLayout: ConstraintLayout = view.findViewById(R.id.detail_key_layout)
}