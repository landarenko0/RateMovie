package com.example.ratemovie.data.usecases

import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import com.example.ratemovie.domain.entities.Movie

class SearchMoviesByNameUseCase(private val moviesListRepository: MoviesListRepository) {

    suspend operator fun invoke(name: String): List<Movie> {
        return moviesListRepository.searchMoviesByName(name)
    }
}