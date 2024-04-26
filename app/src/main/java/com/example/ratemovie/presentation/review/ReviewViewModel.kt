package com.example.ratemovie.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.usecases.AddReviewUseCase
import com.example.ratemovie.domain.usecases.DeleteReviewUseCase
import com.example.ratemovie.domain.usecases.GetUsernameUseCase
import com.example.ratemovie.domain.utils.Globals
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val _shouldCloseFragment = MutableLiveData<Unit>()
    val shouldCloseFragment: LiveData<Unit> get() = _shouldCloseFragment

    private val _shouldShowLoader = MutableLiveData(false)
    val shouldShowLoader: LiveData<Boolean> get() = _shouldShowLoader

    private val userRepository = UserRepositoryImpl()

    private val getUsernameUseCase = GetUsernameUseCase(userRepository)
    private val addReviewUseCase = AddReviewUseCase(userRepository)
    private val deleteReviewUseCase = DeleteReviewUseCase(userRepository)

    fun saveReview(
        reviewText: String,
        grade: Int,
        movieId: Int
    ) {
        _shouldShowLoader.value = true

        val userId = Globals.User?.id ?: return

        viewModelScope.launch {
            val username = getUsernameUseCase(userId)

            val review = Review(reviewText, grade, username)

            addReviewUseCase(review, userId, movieId)

            Globals.User?.addMovieToReviewed(movieId.toString())

            _shouldShowLoader.value = false
            _shouldCloseFragment.value = Unit
        }
    }

    fun deleteReview(review: Review, movieId: Int) {
        _shouldShowLoader.value = true

        val userId = Globals.User?.id ?: return

        viewModelScope.launch {
            deleteReviewUseCase(review, userId, movieId)

            Globals.User?.deleteMovieFromReviewed(movieId.toString())

            _shouldShowLoader.value = false
            _shouldCloseFragment.value = Unit
        }
    }
}