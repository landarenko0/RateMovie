package com.example.ratemovie.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.MovieRepositoryImpl
import com.example.ratemovie.data.UserRepositoryImpl
import com.example.ratemovie.domain.usecases.AddMovieToFavoritesUseCase
import com.example.ratemovie.domain.usecases.CheckUserLikesMovieUseCase
import com.example.ratemovie.domain.usecases.DeleteMovieFromFavoritesUseCase
import com.example.ratemovie.domain.usecases.GetMovieReviewsUseCase
import com.example.ratemovie.domain.usecases.GetUserReviewUseCase
import com.example.ratemovie.domain.entities.Review
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val movieId: Int) : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _userReview = MutableLiveData<Review?>()
    val userReview: LiveData<Review?> get() = _userReview

    private val movieRepository = MovieRepositoryImpl()
    private val userRepository = UserRepositoryImpl()

    private val getMovieReviewsUseCase = GetMovieReviewsUseCase(movieRepository)
    private val checkUserLikesMovieUseCase = CheckUserLikesMovieUseCase(movieRepository)
    private val getUserReviewUseCase = GetUserReviewUseCase(movieRepository)

    private val addMovieToFavoritesUseCase = AddMovieToFavoritesUseCase(userRepository)
    private val deleteMovieFromFavoritesUseCase = DeleteMovieFromFavoritesUseCase(userRepository)

    private fun getMovieReviews(movieId: Int) {
        viewModelScope.launch {
            _reviews.value = getMovieReviewsUseCase(movieId)
        }
    }

    private fun checkUserLikesMovie(userId: String, movieId: Int) {
        viewModelScope.launch {
            _isFavorite.value = checkUserLikesMovieUseCase(userId, movieId)
        }
    }

    private fun getUserReview(userId: String, movieId: Int) {
        viewModelScope.launch {
            _userReview.value = getUserReviewUseCase(userId, movieId)
        }
    }

    fun onFavoriteButtonClicked(movieId: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        if (isFavorite.value != null && isFavorite.value == true) {
            deleteFromFavorites(userId, movieId)
        }
        else {
            addToFavorites(userId, movieId)
        }
    }

    private fun addToFavorites(userId: String, movieId: Int) {
        viewModelScope.launch {
            addMovieToFavoritesUseCase(userId, movieId)
            _isFavorite.value = true
        }
    }

    private fun deleteFromFavorites(userId: String, movieId: Int) {
        viewModelScope.launch {
            deleteMovieFromFavoritesUseCase(userId, movieId)
            _isFavorite.value = false
        }
    }

    fun updateData() {
        getMovieReviews(movieId)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            checkUserLikesMovie(userId, movieId)
            getUserReview(userId, movieId)
        }
    }
}