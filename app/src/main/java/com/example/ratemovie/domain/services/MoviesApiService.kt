package com.example.ratemovie.domain.services

import com.example.ratemovie.domain.api.Api
import com.example.ratemovie.domain.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MoviesApiService {

    @GET("v1.4/movie/search")
    suspend fun searchMoviesByKeywords(
        @Query("query") keywords: String,
        @Query("limit") limit: Int = 20,
        @Header("X-API-KEY") apiKey: String = Api.API_KEY
    ): ApiResponse

    @GET("v1.4/movie")
    suspend fun searchMoviesByGenre(
        @Query("genres.name") genre: String,
        @Header("X-API-KEY") apiKey: String = Api.API_KEY
    ): ApiResponse

    @GET("v1.4/movie")
    suspend fun searchNewMovies(
        @Query("limit") limit: Int = 20,
        @Query("notNullFields") notNullFields: List<String> = listOf("id", "name", "description", "poster.url", "genres.name"),
        @Query("status") status: List<String> = listOf("completed"),
        @Query("year") year: List<String> = listOf("2023-2030"),
        @Query("rating.kp") rating: List<String> = listOf("7-10"),
        @Header("X-API-KEY") apiKey: String = Api.API_KEY
    ): ApiResponse

    @GET("v1.4/movie")
    suspend fun getMoviesByIds(
        @Query("id") ids: List<String>,
        @Header("X-API-KEY") apiKey: String = Api.API_KEY
    ): ApiResponse
}