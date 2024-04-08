package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.UserRepository
import com.example.ratemovie.domain.entities.Review

class EditReviewUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(
        oldReview: Review,
        newReview: Review,
        userId: String,
        movieId: Int
    ) {
        repository.editReview(oldReview, newReview, userId, movieId)
    }
}