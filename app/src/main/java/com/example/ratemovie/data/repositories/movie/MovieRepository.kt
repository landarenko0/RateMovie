package com.example.ratemovie.data.repositories.movie

import com.example.ratemovie.domain.entities.Review

interface MovieRepository {

    suspend fun getMovieReviews(movieId: Int): List<Review>

    suspend fun checkUserLikesMovie(userId: String, movieId: Int): Boolean

    suspend fun getUserReview(userId: String, movieId: Int): Review?

}