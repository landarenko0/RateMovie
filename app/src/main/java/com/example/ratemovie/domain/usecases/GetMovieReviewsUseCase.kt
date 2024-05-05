package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movie.MovieRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Int) =
        movieRepository.getMovieReviews(movieId)
}