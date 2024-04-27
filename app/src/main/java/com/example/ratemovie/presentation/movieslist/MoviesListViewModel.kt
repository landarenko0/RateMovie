package com.example.ratemovie.presentation.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.movieslist.MoviesListRepositoryImpl
import com.example.ratemovie.domain.usecases.GetNewMoviesListUseCase
import com.example.ratemovie.domain.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getNewMoviesListUseCase: GetNewMoviesListUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        viewModelScope.launch {
            _movies.value = getNewMoviesListUseCase()
        }
    }
}