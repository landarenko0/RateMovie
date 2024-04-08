package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.UserRepository
import com.example.ratemovie.domain.entities.Review

class DeleteReviewUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(
        review: Review,
        userId: String,
        movieId: Int
    ) {
        repository.deleteReview(review, userId, movieId)
    }
}