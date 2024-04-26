package com.example.ratemovie.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.SearchFragmentBinding
import com.example.ratemovie.presentation.adapters.SearchMoviesAdapter

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("SearchFragmentBinding was null")

    private val viewModel: SearchViewModel by navGraphViewModels(R.id.navigation_search)

    private val moviesAdapter = SearchMoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setOnQueryTextListener()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        moviesAdapter.onMovieClickListener = { showMovieDetailsFragment(it) }

        binding.rvMovies.adapter = moviesAdapter
    }

    private fun setOnQueryTextListener() {
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keywords: String): Boolean = false

            override fun onQueryTextChange(keywords: String?): Boolean {
                if (keywords.isNullOrBlank()) {
                    moviesAdapter.submitList(emptyList())

                    with(binding) {
                        tvEmptyMoviesList.text = getText(R.string.enter_movie_title)
                        tvEmptyMoviesList.visibility = View.VISIBLE
                    }
                    return true
                }

                viewModel.onSearchTextChanged(keywords)

                return true
            }

        })
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isNotEmpty()) {
                binding.tvEmptyMoviesList.visibility = View.GONE
            }
            else {
                binding.tvEmptyMoviesList.visibility = View.VISIBLE
                binding.tvEmptyMoviesList.text = getText(R.string.no_search_result)
            }

            moviesAdapter.submitList(movies)
        }
    }

    private fun showMovieDetailsFragment(movie: Movie) {
        //val action = SearchFragmentDirections.actionNavigationSearchToNavMovieDetails()
        val action = SearchFragmentDirections.actionNavigationSearchToMovieDetailsFragment(movie)

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}