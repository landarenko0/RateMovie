package com.example.ratemovie.data.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.entities.Review

class AddReviewUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(
        review: Review,
        userId: String,
        movieId: Int
    ) {
        repository.addReview(review, userId, movieId)
    }
}