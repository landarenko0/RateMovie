package com.example.ratemovie.data.usecases

import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import com.example.ratemovie.domain.entities.Movie

class GetNewMoviesListUseCase(private val moviesListRepository: MoviesListRepository) {

    suspend operator fun invoke(): List<Movie> {
        return moviesListRepository.getNewMoviesList()
    }
}