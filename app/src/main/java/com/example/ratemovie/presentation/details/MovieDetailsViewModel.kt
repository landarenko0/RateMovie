package com.example.ratemovie.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.AddMovieToFavoritesUseCase
import com.example.ratemovie.domain.usecases.CheckUserLikesMovieUseCase
import com.example.ratemovie.domain.usecases.DeleteMovieFromFavoritesUseCase
import com.example.ratemovie.domain.usecases.GetMovieReviewsUseCase
import com.example.ratemovie.domain.usecases.GetUserReviewUseCase
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = MovieDetailsViewModel.Factory::class)
class MovieDetailsViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val checkUserLikesMovieUseCase: CheckUserLikesMovieUseCase,
    private val getUserReviewUseCase: GetUserReviewUseCase,
    private val addMovieToFavoritesUseCase: AddMovieToFavoritesUseCase,
    private val deleteMovieFromFavoritesUseCase: DeleteMovieFromFavoritesUseCase
) : ViewModel() {

    private val _reviews = MutableLiveData<RemoteResult<List<Review>>>()
    val reviews: LiveData<RemoteResult<List<Review>>> get() = _reviews

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _userReview = MutableLiveData<RemoteResult<Review?>>()
    val userReview: LiveData<RemoteResult<Review?>> get() = _userReview

    private suspend fun getMovieReviews(movieId: Int) {
        getMovieReviewsUseCase(movieId).collect {
            _reviews.value = it
        }
    }

    private fun checkUserLikesMovie() {
        _isFavorite.value = checkUserLikesMovieUseCase(movieId)
    }

    private suspend fun getUserReview(userId: String, movieId: Int) {
        getUserReviewUseCase(userId, movieId).collect {
            _userReview.value = it
        }
    }

    fun onFavoriteButtonClicked(movieId: Int) {
        val userLikesMovie = _isFavorite.value ?: return

        when {
            userLikesMovie -> deleteFromFavorites(movieId)

            else -> addToFavorites(movieId)
        }
    }

    private fun addToFavorites(movieId: Int) {
        val userId = Globals.User?.id ?: return

        viewModelScope.launch {
            addMovieToFavoritesUseCase(userId, movieId)
            Globals.User?.addMovieToFavorites(movieId.toString())
            _isFavorite.value = true
        }
    }

    private fun deleteFromFavorites(movieId: Int) {
        val userId = Globals.User?.id ?: return

        viewModelScope.launch {
            deleteMovieFromFavoritesUseCase(userId, movieId)
            Globals.User?.deleteMovieFromFavorites(movieId.toString())
            _isFavorite.value = false
        }
    }

    fun updateData() {
        viewModelScope.launch {
            getMovieReviews(movieId)

            val user = Globals.User

            if (user != null) {
                checkUserLikesMovie()
                getUserReview(user.id, movieId)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun build(movieId: Int): MovieDetailsViewModel
    }
}