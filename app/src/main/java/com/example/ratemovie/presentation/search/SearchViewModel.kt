package com.example.ratemovie.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.MoviesListRepositoryImpl
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.usecases.SearchMoviesByNameUseCase
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val repository = MoviesListRepositoryImpl()

    private val searchMoviesByNameUseCase = SearchMoviesByNameUseCase(repository)

    fun searchMoviesByKeywords(name: String) {
        viewModelScope.launch {
            _movies.value = searchMoviesByNameUseCase(name)
        }
    }
}