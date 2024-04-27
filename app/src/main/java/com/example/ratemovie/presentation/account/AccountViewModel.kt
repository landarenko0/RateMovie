package com.example.ratemovie.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.GetMoviesByIdsUseCase
import com.example.ratemovie.domain.usecases.SignOutUseCase
import com.example.ratemovie.domain.entities.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getMoviesByIdsUseCase: GetMoviesByIdsUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _likedMovies = MutableLiveData<List<Movie>?>()
    val likedMovies: LiveData<List<Movie>?> get() = _likedMovies

    private val _reviewedMovies = MutableLiveData<List<Movie>?>()
    val reviewedMovies: LiveData<List<Movie>?> get() = _reviewedMovies

    fun getUserLikedMovies(moviesIds: List<String>) {
        viewModelScope.launch {
            _likedMovies.value = getMoviesByIdsUseCase(moviesIds)
        }
    }

    fun getUserReviewedMovies(moviesIds: List<String>) {
        viewModelScope.launch {
            _reviewedMovies.value = getMoviesByIdsUseCase(moviesIds)
        }
    }

    fun signOut() {
        signOutUseCase()

        _likedMovies.value = null
        _reviewedMovies.value = null
    }
}