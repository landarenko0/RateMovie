package com.example.ratemovie.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.data.usecases.AddReviewUseCase
import com.example.ratemovie.data.usecases.DeleteReviewUseCase
import com.example.ratemovie.data.usecases.GetUsernameUseCase
import com.google.firebase.auth.FirebaseAuth
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
        grade: Float,
        movieId: Int
    ) {
        _shouldShowLoader.value = true

        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw RuntimeException("User was null while adding review")

            val username = getUsernameUseCase(userId)

            val review = Review(reviewText, grade, username)

            addReviewUseCase(review, userId, movieId)

            _shouldShowLoader.value = false
            _shouldCloseFragment.value = Unit
        }
    }

    fun deleteReview(review: Review, movie: Movie) {
        _shouldShowLoader.value = true

        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
                ?: throw RuntimeException("User was null while deleting review")

            deleteReviewUseCase(review, userId, movie.id)

            _shouldShowLoader.value = false
            _shouldCloseFragment.value = Unit
        }
    }
}