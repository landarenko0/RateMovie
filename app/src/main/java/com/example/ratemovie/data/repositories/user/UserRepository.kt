package com.example.ratemovie.data.repositories.user

import com.example.ratemovie.domain.remote.LoginResult
import com.example.ratemovie.domain.remote.RegistrationResult
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.entities.User
import com.example.ratemovie.domain.remote.RemoteResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUser(userId: String?): Flow<RemoteResult<User?>>

    suspend fun signIn(email: String, password: String): Flow<RemoteResult<LoginResult>>

    fun signOut()

    suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): Flow<RemoteResult<RegistrationResult>>

    suspend fun addMovieToFavorites(userId: String, movieId: Int): Flow<RemoteResult<Unit>>

    suspend fun deleteMovieFromFavorites(userId: String, movieId: Int): Flow<RemoteResult<Unit>>

    suspend fun addReview(review: Review, userId: String, movieId: Int): Flow<RemoteResult<Unit>>

    suspend fun deleteReview(review: Review, userId: String, movieId: Int): Flow<RemoteResult<Unit>>
}