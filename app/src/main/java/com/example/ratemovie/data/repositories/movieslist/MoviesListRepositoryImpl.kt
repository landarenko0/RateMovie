package com.example.ratemovie.data.repositories.movieslist

import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.remote.ApiResponse
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.services.MoviesApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class MoviesListRepositoryImpl @Inject constructor(
    private val service: MoviesApiService
) : MoviesListRepository {

    override suspend fun getNewMoviesList(): Flow<RemoteResult<List<Movie>>> {
        return getMoviesBy { service.searchNewMovies() }
    }

    override suspend fun searchMoviesByName(name: String): Flow<RemoteResult<List<Movie>>> {
        return getMoviesBy { service.searchMoviesByKeywords(name) }
    }

    override suspend fun getMoviesByIds(ids: List<String>): Flow<RemoteResult<List<Movie>>> {
        return getMoviesBy { service.getMoviesByIds(ids) }
    }

    private suspend fun getMoviesBy(
        action: suspend () -> Response<ApiResponse>
    ): Flow<RemoteResult<List<Movie>>> =
        flow {
            emit(RemoteResult.Loading)

            try {
                val response = action()

                when {
                    response.isSuccessful && response.body() is ApiResponse -> {
                        val movies = (response.body() as ApiResponse).movies
                        emit(RemoteResult.Success(movies))
                    }
                }
            } catch (ex: Exception) {
                emit(RemoteResult.Error("Не удалось загрузить фильмы"))
            }
        }.flowOn(Dispatchers.IO)
}