package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movie.MovieRepository
import com.example.ratemovie.domain.entities.Review
import javax.inject.Inject

class GetUserReviewUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(
        userId: String,
        movieId: Int
    ): Review? {
        return movieRepository.getUserReview(userId, movieId)
    }
}