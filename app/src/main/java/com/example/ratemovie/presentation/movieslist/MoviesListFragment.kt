package com.example.ratemovie.presentation.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.MoviesListFragmentBinding
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.presentation.recyclerview.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: MoviesListFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("MoviesListFragmentBinding was null")

    private val viewModel: MoviesListViewModel by hiltNavGraphViewModels(R.id.navigation_movies)

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
        viewModel.movies.observe(viewLifecycleOwner) { result ->
            when(result) {
                RemoteResult.Loading -> { }

                is RemoteResult.Success -> {
                    if (result.data != null) {
                        moviesAdapter.submitList(result.data)
                    }
                }

                is RemoteResult.Error -> showToast(getString(R.string.request_failed))
            }
        }
    }

    private fun showMovieDetailsFragment(movie: Movie) {
        val action =
            MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailsFragment(movie)

        findNavController().navigate(action)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}