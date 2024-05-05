package com.example.ratemovie.data.repositories.movie

import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieReviews(movieId: Int): Flow<RemoteResult<List<Review>>>

    fun checkUserLikesMovie(movieId: Int): Boolean

    suspend fun getUserReview(userId: String, movieId: Int): Flow<RemoteResult<Review?>>
}