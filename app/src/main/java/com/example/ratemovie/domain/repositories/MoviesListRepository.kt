package com.example.ratemovie.domain.repositories

import com.example.ratemovie.domain.entities.Movie

interface MoviesListRepository {

    suspend fun getNewMoviesList(): List<Movie>

    suspend fun searchMoviesByName(name: String): List<Movie>

    suspend fun getMoviesByIds(ids: List<String>): List<Movie>

}