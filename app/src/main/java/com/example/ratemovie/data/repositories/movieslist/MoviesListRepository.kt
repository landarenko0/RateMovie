package com.example.ratemovie.data.repositories.movieslist

import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.remote.RemoteResult
import kotlinx.coroutines.flow.Flow

interface MoviesListRepository {

    suspend fun getNewMoviesList(): Flow<RemoteResult<List<Movie>>>

    suspend fun searchMoviesByName(name: String): Flow<RemoteResult<List<Movie>>>

    suspend fun getMoviesByIds(ids: List<String>): Flow<RemoteResult<List<Movie>>>
}