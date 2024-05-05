package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movie.MovieRepository
import javax.inject.Inject

class CheckUserLikesMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(movieId: Int) = movieRepository.checkUserLikesMovie(movieId)
}