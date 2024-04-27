package com.example.ratemovie.data.repositories.movieslist

import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.services.MoviesApiService
import javax.inject.Inject

class MoviesListRepositoryImpl @Inject constructor(
    private val service: MoviesApiService
) : MoviesListRepository {

    override suspend fun getNewMoviesList(): List<Movie> {
        return service.searchNewMovies().movies
    }

    override suspend fun searchMoviesByName(name: String): List<Movie> {
        val movies = service.searchMoviesByKeywords(name).movies

        return movies.filter {
            it.title.isNotEmpty() && it.description.isNotEmpty() && it.posterUrl != null && it.posterUrl.url != null
        }.sortedByDescending { it.rating.kp }
    }

    override suspend fun getMoviesByIds(ids: List<String>): List<Movie> {
        return if (ids.isEmpty()) emptyList() else service.getMoviesByIds(ids).movies
    }
}