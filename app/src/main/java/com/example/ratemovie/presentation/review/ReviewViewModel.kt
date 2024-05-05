package com.example.ratemovie.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.usecases.AddReviewUseCase
import com.example.ratemovie.domain.usecases.DeleteReviewUseCase
import com.example.ratemovie.domain.utils.Globals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val addReviewUseCase: AddReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase

) : ViewModel() {

    private val _result = MutableLiveData<RemoteResult<Unit>>()
    val result: LiveData<RemoteResult<Unit>> = _result

    fun saveReview(
        reviewText: String,
        grade: Int,
        movieId: Int
    ) {
        val userId = Globals.User?.id ?: return
        val username = Globals.User!!.username
        val review = Review(reviewText, grade, username)

        viewModelScope.launch {
            addReviewUseCase(review, userId, movieId).collect {
                _result.value = it

                if (it is RemoteResult.Success) {
                    Globals.User?.addMovieToReviewed(movieId.toString())
                }
            }
        }
    }

    fun deleteReview(review: Review, movieId: Int) {
        val userId = Globals.User?.id ?: return

        viewModelScope.launch {
            deleteReviewUseCase(review, userId, movieId).collect {
                _result.value = it

                if (it is RemoteResult.Success) {
                    Globals.User?.deleteMovieFromReviewed(movieId.toString())
                }
            }
        }
    }
}