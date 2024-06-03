package com.example.ratemovie.data.repositories.movie

import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MovieRepositoryImpl : MovieRepository {

    override suspend fun getMovieReviews(movieId: Int): Flow<RemoteResult<List<Review>>> =
        flow {
            emit(RemoteResult.Loading)

            try {
                val snapshot = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("Movies/$movieId/reviews")
                    .get()
                    .await()

                val reviews = mutableListOf<Review>()

                snapshot
                    .children
                    .forEach {
                        reviews.add(
                            it.getValue(Review::class.java)
                                ?: throw RuntimeException("Полученный объект не является отзывом")
                        )
                    }

                emit(RemoteResult.Success(reviews))
            } catch (ex: Exception) {
                emit(RemoteResult.Error("Произошла ошибка при получении отзывов"))
            }
        }.flowOn(Dispatchers.IO)

    override fun checkUserLikesMovie(movieId: Int): Boolean {
        return Globals.User.value?.liked?.any { it == movieId.toString() }
            ?: throw RuntimeException("User was null while checking")
    }

    override suspend fun getUserReview(userId: String, movieId: Int): Flow<RemoteResult<Review?>> =
        flow {
            emit(RemoteResult.Loading)

            try {
                val snapshot = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("Movies/$movieId/reviews/$userId")
                    .get()
                    .await()

                val review = snapshot.getValue(Review::class.java)

                emit(RemoteResult.Success(review))
            } catch (ex: Exception) {
                emit(RemoteResult.Error("Произошла ошибка при получении отзыва"))
            }
        }.flowOn(Dispatchers.IO)
}