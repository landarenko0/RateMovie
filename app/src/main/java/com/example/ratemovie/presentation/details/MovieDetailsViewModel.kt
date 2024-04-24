package com.example.ratemovie.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.movie.MovieRepositoryImpl
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
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

    private val _shouldShowLoader = MutableLiveData(false)
    val shouldShowLoader: LiveData<Boolean> get() = _shouldShowLoader

    private val movieRepository = MovieRepositoryImpl()
    private val userRepository = UserRepositoryImpl()

    private val getMovieReviewsUseCase = GetMovieReviewsUseCase(movieRepository)
    private val checkUserLikesMovieUseCase = CheckUserLikesMovieUseCase(movieRepository)
    private val getUserReviewUseCase = GetUserReviewUseCase(movieRepository)

    private val addMovieToFavoritesUseCase = AddMovieToFavoritesUseCase(userRepository)
    private val deleteMovieFromFavoritesUseCase = DeleteMovieFromFavoritesUseCase(userRepository)

    private suspend fun getMovieReviews(movieId: Int) {
        _reviews.value = getMovieReviewsUseCase(movieId)
    }

    private suspend fun checkUserLikesMovie(userId: String, movieId: Int) {
        _isFavorite.value = checkUserLikesMovieUseCase(userId, movieId)
    }

    private suspend fun getUserReview(userId: String, movieId: Int) {
        _userReview.value = getUserReviewUseCase(userId, movieId)
    }

    fun addToFavorites(movieId: Int) {
        _shouldShowLoader.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            addMovieToFavoritesUseCase(userId, movieId)
            _isFavorite.value = true
            _shouldShowLoader.value = false
        }
    }

    fun deleteFromFavorites(movieId: Int) {
        _shouldShowLoader.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            deleteMovieFromFavoritesUseCase(userId, movieId)
            _isFavorite.value = false
            _shouldShowLoader.value = false
        }
    }

    fun updateData() {
        viewModelScope.launch {
            _shouldShowLoader.value = true

            getMovieReviews(movieId)

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                checkUserLikesMovie(userId, movieId)
                getUserReview(userId, movieId)
            }

            _shouldShowLoader.value = false
        }
    }
}