package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import javax.inject.Inject

class SearchMoviesByNameUseCase @Inject constructor(
    private val moviesListRepository: MoviesListRepository
) {

    suspend operator fun invoke(name: String) = moviesListRepository.searchMoviesByName(name)
}