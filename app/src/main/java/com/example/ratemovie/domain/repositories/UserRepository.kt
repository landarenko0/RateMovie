package com.example.ratemovie.domain.repositories

import com.example.ratemovie.domain.entities.LoginResult
import com.example.ratemovie.domain.entities.RegistrationResult
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.entities.User

interface UserRepository {

    suspend fun getUser(userId: String?): User?

    suspend fun getUsername(userId: String): String

    suspend fun signIn(
        email: String,
        password: String
    ): LoginResult

    fun signOut()

    suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): RegistrationResult

    suspend fun addMovieToFavorites(
        userId: String,
        movieId: Int
    )

    suspend fun deleteMovieFromFavorites(
        userId: String,
        movieId: Int
    )

    suspend fun addReview(
        review: Review,
        userId: String,
        movieId: Int
    )

    suspend fun deleteReview(
        review: Review,
        userId: String,
        movieId: Int
    )

    suspend fun editReview(
        oldReview: Review,
        newReview: Review,
        userId: String,
        movieId: Int
    )

}