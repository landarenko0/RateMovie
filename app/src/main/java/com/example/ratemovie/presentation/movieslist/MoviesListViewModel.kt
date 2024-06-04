package com.example.ratemovie.presentation.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.GetNewMoviesListUseCase
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.remote.RemoteResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getNewMoviesListUseCase: GetNewMoviesListUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<RemoteResult<List<Movie>?>>()
    val movies: LiveData<RemoteResult<List<Movie>?>> = _movies

    init {
        viewModelScope.launch {
            getNewMoviesListUseCase().collect {
                _movies.value = it
            }
        }
    }
}