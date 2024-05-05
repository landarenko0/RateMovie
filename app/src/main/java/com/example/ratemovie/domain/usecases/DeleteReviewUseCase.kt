package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.entities.Review
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(review: Review, userId: String, movieId: Int) =
        repository.deleteReview(review, userId, movieId)
}