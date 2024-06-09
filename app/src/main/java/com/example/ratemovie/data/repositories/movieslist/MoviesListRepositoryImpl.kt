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

    override suspend fun getNewMoviesList(): Flow<RemoteResult<List<Movie>>> =
        getMoviesBy { service.searchNewMovies() }

    override suspend fun searchMoviesByName(name: String): Flow<RemoteResult<List<Movie>>> =
        getMoviesBy { service.searchMoviesByKeywords(name) }

    override suspend fun getMoviesByIds(ids: List<String>): Flow<RemoteResult<List<Movie>>> =
        getMoviesBy { service.getMoviesByIds(ids) }

    private suspend fun getMoviesBy(
        getMovies: suspend () -> Response<ApiResponse>
    ): Flow<RemoteResult<List<Movie>>> =
        flow {
            emit(RemoteResult.Loading)

            try {
                val response = getMovies()

                when {
                    response.isSuccessful && response.body() is ApiResponse -> {
                        val movies = (response.body() as ApiResponse).movies.filter {
                            it.title != "" && it.description != "" && it.posterUrl != null && it.genres.isNotEmpty()
                        }

                        emit(RemoteResult.Success(movies))
                    }

                    else -> emit(RemoteResult.Error("Не удалось выполнить запрос"))
                }
            } catch (ex: Exception) {
                emit(RemoteResult.Error("Не удалось загрузить фильмы"))
            }
        }.flowOn(Dispatchers.IO)
}