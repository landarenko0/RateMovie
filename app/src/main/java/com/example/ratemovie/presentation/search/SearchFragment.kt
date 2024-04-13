package com.example.ratemovie.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.SearchFragmentBinding
import com.example.ratemovie.presentation.adapters.SearchMoviesAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("SearchFragmentBinding was null")

    private val viewModel: SearchViewModel by navGraphViewModels(R.id.navigation_search)

    private val moviesAdapter = SearchMoviesAdapter()

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        setOnQueryTextListener()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        moviesAdapter.onMovieClickListener = { showMovieDetailsFragment(it) }

        binding.rvMovies.adapter = moviesAdapter
    }

    private fun setOnQueryTextListener() {
        // TODO: Показывать лоадер при загрузке
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keywords: String): Boolean = false

            override fun onQueryTextChange(keywords: String?): Boolean {
                searchJob?.cancel()

                if (keywords.isNullOrBlank()) {
                    moviesAdapter.submitList(emptyList())
                    binding.tvEmptyMoviesList.text = getText(R.string.enter_movie_title)
                    binding.tvEmptyMoviesList.visibility = View.VISIBLE
                    return true
                }

                searchJob = lifecycleScope.launch {
                    delay(SEARCH_DELAY_MS)
                    viewModel.searchMoviesByKeywords(keywords)
                }

                return true
            }

        })
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isNotEmpty()) {
                binding.tvEmptyMoviesList.visibility = View.GONE
            } else {
                binding.tvEmptyMoviesList.visibility = View.VISIBLE
                binding.tvEmptyMoviesList.text = getText(R.string.no_search_result)
            }

            moviesAdapter.submitList(movies)
        }
    }

    private fun showMovieDetailsFragment(movie: Movie) {
        val action = SearchFragmentDirections.actionNavigationSearchToMovieDetailsFragment(movie)

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SEARCH_DELAY_MS = 750L
    }
}