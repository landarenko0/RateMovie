package com.example.ratemovie.presentation.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.movieslist.MoviesListRepositoryImpl
import com.example.ratemovie.data.usecases.GetNewMoviesListUseCase
import com.example.ratemovie.domain.entities.Movie
import kotlinx.coroutines.launch

class MoviesListViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val repository = MoviesListRepositoryImpl()

    private val getNewMoviesListUseCase = GetNewMoviesListUseCase(repository)

    init {
        viewModelScope.launch {
            _movies.value = getNewMoviesListUseCase()
        }
    }
}