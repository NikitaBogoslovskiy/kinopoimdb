package com.example.kinopoimdb.ui.movies

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.kinopoimdb.R
import com.example.kinopoimdb.databinding.FragmentMoviesBinding
import com.example.kinopoimdb.ui.persons.PersonsViewModel

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.movies_list)
        val moviesViewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        recyclerView.adapter = moviesViewModel.moviesViewAdapter
        view.findViewById<Button>(R.id.movie_search_button).setOnClickListener {
            val progressBar = view.findViewById<ProgressBar>(R.id.movies_search_progress)
            val messageTextView = view.findViewById<TextView>(R.id.message_text_view)
            messageTextView.visibility = View.GONE
            moviesViewModel.findMovies(
                activateProgressBarCallback = {
                    progressBar.visibility = View.VISIBLE
                },
                disableProgressBarCallback = {
                    progressBar.visibility = View.GONE
                },
                errorMessageCallback = {
                    messageTextView.text = it
                    messageTextView.visibility = View.VISIBLE
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}