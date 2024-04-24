package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movie.MovieRepository

class CheckUserLikesMovieUseCase(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(
        userId: String,
        movieId: Int
    ): Boolean {
        return movieRepository.checkUserLikesMovie(userId, movieId)
    }
}