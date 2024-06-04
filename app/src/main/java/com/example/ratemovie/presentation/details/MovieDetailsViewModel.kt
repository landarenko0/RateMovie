package com.example.ratemovie.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.AddMovieToFavoritesUseCase
import com.example.ratemovie.domain.usecases.DeleteMovieFromFavoritesUseCase
import com.example.ratemovie.domain.usecases.GetMovieReviewsUseCase
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.RuntimeException

@HiltViewModel(assistedFactory = MovieDetailsViewModel.Factory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val addMovieToFavoritesUseCase: AddMovieToFavoritesUseCase,
    private val deleteMovieFromFavoritesUseCase: DeleteMovieFromFavoritesUseCase
) : ViewModel() {

    private val _reviews = MutableLiveData<RemoteResult<List<Review>?>>(RemoteResult.Success(null))
    val reviews: LiveData<RemoteResult<List<Review>?>> = _reviews

    private val _isFavorite = MutableLiveData<RemoteResult<Boolean?>>(RemoteResult.Success(null))
    val isFavorite: LiveData<RemoteResult<Boolean?>> = _isFavorite

    init {
        updateData()
    }

    private suspend fun getMovieReviews() {
        getMovieReviewsUseCase(movieId).collect {
            _reviews.value = it
        }
    }

    private fun checkUserLikesMovie() = movieId.toString() in User.value!!.liked

    fun onFavoriteButtonClicked() {
        if (checkUserLikesMovie()) deleteFromFavorites()
        else addToFavorites()
    }

    private fun addToFavorites() {
        val userId = User.value?.id ?: throw RuntimeException("User is null")

        viewModelScope.launch {
            addMovieToFavoritesUseCase(userId, movieId).collect {
                _isFavorite.value = it
            }

            User.value!!.addMovieToFavorites(movieId.toString())
        }
    }

    private fun deleteFromFavorites() {
        val userId = User.value?.id ?: throw RuntimeException("User is null")

        viewModelScope.launch {
            deleteMovieFromFavoritesUseCase(userId, movieId).collect {
                _isFavorite.value = it
            }

            User.value!!.deleteMovieFromFavorites(movieId.toString())
        }
    }

    fun updateData() {
        viewModelScope.launch {
            getMovieReviews()

            if (User.value != null) {
                _isFavorite.value = RemoteResult.Success(movieId.toString() in User.value!!.liked)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun build(movieId: Int): MovieDetailsViewModel
    }
}