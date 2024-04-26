package com.example.ratemovie.presentation.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.MoviesListFragmentBinding
import com.example.ratemovie.presentation.adapters.MoviesAdapter

class MoviesListFragment : Fragment() {

    private var _binding: MoviesListFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("MoviesListFragmentBinding was null")

    private val viewModel: MoviesListViewModel by navGraphViewModels(R.id.navigation_movies)

    private val moviesAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MoviesListFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        moviesAdapter.onMovieClickListener = { showMovieDetailsFragment(it) }

        binding.rvMovies.adapter = moviesAdapter
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            moviesAdapter.submitList(movies)
        }
    }

    private fun showMovieDetailsFragment(movie: Movie) {
        //val action = MoviesListFragmentDirections.actionNavigationMoviesToNavMovieDetails()
        val action =
            MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(movie)

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}