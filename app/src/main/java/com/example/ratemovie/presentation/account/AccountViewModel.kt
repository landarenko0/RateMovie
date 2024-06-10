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

    private val _likedMovies =
        MutableLiveData<RemoteResult<List<Movie>?>>(RemoteResult.Success(null))
    val likedMovies: LiveData<RemoteResult<List<Movie>?>> = _likedMovies

    private val _reviewedMovies =
        MutableLiveData<RemoteResult<List<Movie>?>>(RemoteResult.Success(null))
    val reviewedMovies: LiveData<RemoteResult<List<Movie>?>> = _reviewedMovies

    fun getUserLikedMovies(moviesIds: List<String>) {
        if (moviesIds.isEmpty()) {
            _likedMovies.value = RemoteResult.Success(emptyList())
        } else {
            getMovies(moviesIds, _likedMovies)
        }
    }

    fun getUserReviewedMovies(moviesIds: List<String>) {
        if (moviesIds.isEmpty()) {
            _reviewedMovies.value = RemoteResult.Success(emptyList())
        } else {
            getMovies(moviesIds, _reviewedMovies)
        }
    }

    private fun getMovies(
        moviesIds: List<String>,
        destination: MutableLiveData<RemoteResult<List<Movie>?>>
    ) {
        viewModelScope.launch {
            getMoviesByIdsUseCase(moviesIds).collect {
                destination.value = it
            }
        }
    }

    fun signOut() {
        signOutUseCase()

        _likedMovies.value = RemoteResult.Success(null)
        _reviewedMovies.value = RemoteResult.Success(null)
    }
}