package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.MoviesListRepository
import com.example.ratemovie.domain.entities.Movie

class GetNewMoviesListUseCase(private val moviesListRepository: MoviesListRepository) {

    suspend operator fun invoke(): List<Movie> {
        return moviesListRepository.getNewMoviesList()
    }
}