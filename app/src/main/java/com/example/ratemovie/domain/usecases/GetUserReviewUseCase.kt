package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movie.MovieRepository
import javax.inject.Inject

class GetUserReviewUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(userId: String, movieId: Int) =
        movieRepository.getUserReview(userId, movieId)
}