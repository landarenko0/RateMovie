package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.MovieRepository
import com.example.ratemovie.domain.entities.Review

class GetUserReviewUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(
        userId: String,
        movieId: Int
    ): Review? {
        return movieRepository.getUserReview(userId, movieId)
    }
}