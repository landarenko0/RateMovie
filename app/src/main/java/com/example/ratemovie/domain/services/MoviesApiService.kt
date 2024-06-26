package com.example.ratemovie.domain.services

import com.example.ratemovie.domain.remote.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {

    @GET("v1.4/movie/search")
    suspend fun searchMoviesByKeywords(
        @Query("query") keywords: String,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse>

    @GET("v1.4/movie")
    suspend fun searchMoviesByGenre(
        @Query("genres.name") genre: String
    ): Response<ApiResponse>

    @GET("v1.4/movie")
    suspend fun searchNewMovies(
        @Query("limit") limit: Int = 20,
        @Query("notNullFields") notNullFields: List<String> = listOf("id", "name", "description", "poster.url", "genres.name"),
        @Query("status") status: List<String> = listOf("completed"),
        @Query("year") year: List<String> = listOf("2023-2030"),
        @Query("rating.kp") rating: List<String> = listOf("7-10"),
        @Query("typeNumber") typeNumber: List<String> = listOf("!3", "!5"),
        @Query("isSeries") isSeries: List<String> = listOf("true"),
        @Query("genres.name") genres: List<String> = listOf("!документальный", "!игра", "!реальное ТВ"),
        @Query("countries.name") countries: List<String> = listOf("Россия")
    ): Response<ApiResponse>

    @GET("v1.4/movie")
    suspend fun getMoviesByIds(
        @Query("id") ids: List<String>
    ): Response<ApiResponse>
}