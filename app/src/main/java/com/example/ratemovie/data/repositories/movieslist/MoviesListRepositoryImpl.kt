package com.example.ratemovie.data.repositories.movieslist

import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.api.Api
import com.example.ratemovie.domain.services.MoviesApiService
import com.example.ratemovie.domain.retrofit.RetrofitBuilder

class MoviesListRepositoryImpl : MoviesListRepository {

    override suspend fun getNewMoviesList(): List<Movie> {
        val retrofit = RetrofitBuilder.build(Api.BASE_URL)
        val service = retrofit.create(MoviesApiService::class.java)

        return service.searchNewMovies().movies
    }

    override suspend fun searchMoviesByName(name: String): List<Movie> {
        val retrofit = RetrofitBuilder.build(Api.BASE_URL)
        val service = retrofit.create(MoviesApiService::class.java)

        val movies = service.searchMoviesByKeywords(name).movies

        return movies.filter {
            it.title.isNotEmpty() && it.description.isNotEmpty() && it.posterUrl != null && it.posterUrl.url != null
        }.sortedByDescending { it.rating.kp }
    }

    override suspend fun getMoviesByIds(ids: List<String>): List<Movie> {
        val retrofit = RetrofitBuilder.build(Api.BASE_URL)
        val service = retrofit.create(MoviesApiService::class.java)

        return if (ids.isEmpty()) emptyList() else service.getMoviesByIds(ids).movies
    }
}