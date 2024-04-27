package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import com.example.ratemovie.domain.entities.Movie
import javax.inject.Inject

class GetNewMoviesListUseCase @Inject constructor(
    private val moviesListRepository: MoviesListRepository
) {

    suspend operator fun invoke(): List<Movie> {
        return moviesListRepository.getNewMoviesList()
    }
}