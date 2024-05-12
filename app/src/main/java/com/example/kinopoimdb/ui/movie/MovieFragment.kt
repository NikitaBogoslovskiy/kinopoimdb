package com.example.kinopoimdb.ui.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.R
import com.example.kinopoimdb.databinding.FragmentMovieBinding
import org.w3c.dom.Text

class MovieFragment : Fragment() {
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.movie_details)
        val movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        recyclerView.adapter = movieViewModel.movieDetailsViewAdapter
        val messageDetailsMessage = view.findViewById<TextView>(R.id.movie_details_message)
        messageDetailsMessage.visibility = View.GONE
        val progressBar = view.findViewById<ProgressBar>(R.id.movie_details_progress)
        val movieTitleView = view.findViewById<TextView>(R.id.movie_title_view)
        movieViewModel.findMovie(
            id = arguments?.get("id") as Long,
            setTitleCallback = {
                movieTitleView.text = it
                movieTitleView.visibility = View.VISIBLE
            },
            activateProgressBarCallback = {
                progressBar.visibility = View.VISIBLE
            },
            disableProgressBarCallback = {
                progressBar.visibility = View.GONE
            },
            errorMessageCallback = {
                messageDetailsMessage.text = it
                messageDetailsMessage.visibility = View.VISIBLE
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}