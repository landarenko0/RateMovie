package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movie.MovieRepository
import com.example.ratemovie.domain.entities.Review

class GetMovieReviewsUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Int): List<Review> {
        return movieRepository.getMovieReviews(movieId)
    }
}