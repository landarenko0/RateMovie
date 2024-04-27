package com.example.ratemovie.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.usecases.SearchMoviesByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesByNameUseCase: SearchMoviesByNameUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private var searchJob: Job? = null

    private suspend fun searchMoviesByKeywords(name: String) {
        _movies.value = searchMoviesByNameUseCase(name)
    }

    fun onSearchTextChanged(keywords: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY_MS)
            searchMoviesByKeywords(keywords)
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS = 750L
    }
}