package com.example.ratemovie.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.usecases.AddReviewUseCase
import com.example.ratemovie.domain.usecases.DeleteReviewUseCase
import com.example.ratemovie.domain.usecases.GetUserReviewUseCase
import com.example.ratemovie.domain.utils.Globals.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReviewViewModel.Factory::class)
class ReviewViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val addReviewUseCase: AddReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val getUserReviewUseCase: GetUserReviewUseCase

) : ViewModel() {

    private val _userReview = MutableLiveData<RemoteResult<Review?>>(RemoteResult.Success(null))
    val userReview: LiveData<RemoteResult<Review?>> = _userReview

    private val _result = MutableLiveData<RemoteResult<Boolean?>>()
    val result: LiveData<RemoteResult<Boolean?>> = _result

    private val _symbolsLeft = MutableLiveData(MAX_TEXT_LENGTH)
    val symbolsLeft: LiveData<Int> = _symbolsLeft

    init {
        checkUserReview()
    }

    private fun checkUserReview() {
        if (User.value == null) return

        val userLeftReview = movieId.toString() in User.value!!.reviewed

        if (userLeftReview) {
            val userId = User.value!!.id

            viewModelScope.launch {
                getUserReviewUseCase(userId, movieId).collect {
                    _userReview.value = it
                }
            }
        }
    }

    fun saveReview(
        reviewText: String,
        grade: Int
    ) {
        if (grade < 1) {
            _result.value = RemoteResult.Error("Оценка не может быть меньше 1")
            return
        }

        val userId = User.value?.id ?: throw RuntimeException("User in null")
        val username = User.value!!.username
        val review = Review(reviewText, grade, username)

        viewModelScope.launch {
            addReviewUseCase(review, userId, movieId).collect {
                _result.value = it

                if (it is RemoteResult.Success) {
                    User.value!!.addMovieToReviewed(movieId.toString())
                }
            }
        }
    }

    fun deleteReview() {
        val userId = User.value?.id ?: throw RuntimeException("User in null")

        viewModelScope.launch {
            val review = _userReview.value as RemoteResult.Success
            deleteReviewUseCase(review.data!!, userId, movieId).collect {
                _result.value = it

                if (it is RemoteResult.Success) {
                    User.value!!.deleteMovieFromReviewed(movieId.toString())
                }
            }
        }
    }

    fun onReviewTextChanged(text: CharSequence) {
        _symbolsLeft.value = MAX_TEXT_LENGTH - text.length
    }

    @AssistedFactory
    interface Factory {
        fun build(movieId: Int): ReviewViewModel
    }

    companion object {
        private const val MAX_TEXT_LENGTH = 1000
    }
}