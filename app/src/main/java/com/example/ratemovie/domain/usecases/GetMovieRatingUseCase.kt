package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.MovieRepository

class GetMovieRatingUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(movieId: Int): Float? {
        return movieRepository.getMovieRating(movieId)
    }
}