package com.example.ratemovie.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.GetMoviesByIdsUseCase
import com.example.ratemovie.domain.usecases.SignOutUseCase
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.remote.RemoteResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getMoviesByIdsUseCase: GetMoviesByIdsUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _likedMovies = MutableLiveData<RemoteResult<List<Movie>?>>()
    val likedMovies: LiveData<RemoteResult<List<Movie>?>> get() = _likedMovies

    private val _reviewedMovies = MutableLiveData<RemoteResult<List<Movie>?>>()
    val reviewedMovies: LiveData<RemoteResult<List<Movie>?>> get() = _reviewedMovies

    fun getUserLikedMovies(moviesIds: List<String>) {
        viewModelScope.launch {
            getMoviesByIdsUseCase(moviesIds).collect {
                _likedMovies.value = it
            }
        }
    }

    fun getUserReviewedMovies(moviesIds: List<String>) {
        viewModelScope.launch {
            getMoviesByIdsUseCase(moviesIds).collect {
                _reviewedMovies.value = it
            }
        }
    }

    fun signOut() {
        signOutUseCase()

        _likedMovies.value = RemoteResult.Success(null)
        _reviewedMovies.value = RemoteResult.Success(null)
    }
}