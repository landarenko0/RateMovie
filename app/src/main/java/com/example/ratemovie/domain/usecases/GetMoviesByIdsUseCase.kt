package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import com.example.ratemovie.domain.entities.Movie

class GetMoviesByIdsUseCase(private val moviesListRepository: MoviesListRepository) {

    suspend operator fun invoke(ids: List<String>): List<Movie> {
        return moviesListRepository.getMoviesByIds(ids)
    }
}