package com.example.ratemovie.data

import com.example.ratemovie.domain.repositories.MovieRepository
import com.example.ratemovie.domain.entities.Review
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MovieRepositoryImpl : MovieRepository {

    override suspend fun getMovieReviews(movieId: Int): List<Review> {
        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId/reviews")
            .get()
            .await()

        val reviews = mutableListOf<Review?>()

        snapshot
            .children
            .forEach {
                reviews.add(it.getValue(Review::class.java))
            }

        return reviews.filterNotNull()
    }

    override suspend fun checkUserLikesMovie(userId: String, movieId: Int): Boolean {
        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/$userId/liked")
            .get()
            .await()

        val likedMovies = snapshot.value as? List<String>

        return likedMovies?.any { it == movieId.toString() } ?: false
    }

    override suspend fun getUserReview(userId: String, movieId: Int): Review? {
        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId/reviews/$userId")
            .get()
            .await()

        return snapshot.getValue(Review::class.java)
    }
}