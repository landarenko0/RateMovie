package com.example.ratemovie.data.repositories.user

import com.example.ratemovie.domain.entities.User
import com.example.ratemovie.domain.remote.LoginResult
import com.example.ratemovie.domain.remote.RegistrationResult
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlin.Exception

class UserRepositoryImpl : UserRepository {

    override suspend fun getUser(userId: String?) = flow {
        emit(RemoteResult.Loading)

        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/${userId}")
            .get()
            .await()

        emit(RemoteResult.Success(snapshot.getValue(User::class.java)))
    }.flowOn(Dispatchers.IO)

    override suspend fun signIn(email: String, password: String): Flow<RemoteResult<LoginResult>> =
        flow<RemoteResult<LoginResult>> {
            if (email.isEmpty() || password.isEmpty()) {
                emit(RemoteResult.Error(LoginResult.Error.EMPTY_FIELDS))
                return@flow
            }

            try {
                FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .await()
            } catch (e: Exception) {
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> emit(RemoteResult.Error(LoginResult.Error.INVALID_CREDENTIALS))
                    is FirebaseAuthInvalidUserException -> emit(RemoteResult.Error(LoginResult.Error.INVALID_CREDENTIALS))
                    else -> emit(RemoteResult.Error(LoginResult.Error.DEFAULT))
                }

                return@flow
            }

            val userId: String
            val user: User

            try {
                userId = FirebaseAuth.getInstance().currentUser!!.uid

                val snapshot = FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("Users/${userId}")
                    .get()
                    .await()

                user = snapshot.getValue(User::class.java)!!
            } catch (e: Exception) {
                emit(RemoteResult.Error(LoginResult.Error.DEFAULT))
                return@flow
            }

            emit(RemoteResult.Success(LoginResult.Success(user)))
        }.flowOn(Dispatchers.IO)

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
        Globals.User = null
    }

    override suspend fun signUp(username: String, email: String, password: String) = flow {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            emit(RemoteResult.Error(RegistrationResult.Error.EMPTY_FIELDS))
            return@flow
        }

        try {
            val result = FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            FirebaseDatabase
                .getInstance()
                .reference
                .child("Users/${result.user!!.uid}")
                .setValue(User(username, email))
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthWeakPasswordException -> emit(RemoteResult.Error(RegistrationResult.Error.WEAK_PASSWORD))
                is FirebaseAuthInvalidCredentialsException -> emit(RemoteResult.Error(RegistrationResult.Error.INVALID_CREDENTIALS))
                is FirebaseAuthUserCollisionException -> emit(RemoteResult.Error(RegistrationResult.Error.EMAIL_COLLISION))
                else -> emit(RemoteResult.Error(RegistrationResult.Error.DEFAULT))
            }

            return@flow
        }

        val userId: String
        val user: User

        try {
            userId = FirebaseAuth.getInstance().currentUser!!.uid

            val snapshot = FirebaseDatabase
                .getInstance()
                .reference
                .child("Users/${userId}")
                .get()
                .await()

            user = snapshot.getValue(User::class.java)!!
        } catch (e: Exception) {
            emit(RemoteResult.Error(RegistrationResult.Error.DEFAULT))
            return@flow
        }

        emit(RemoteResult.Success(RegistrationResult.Success(user)))
    }

    override suspend fun addMovieToFavorites(userId: String, movieId: Int) = flow {
        emit(RemoteResult.Loading)

        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/$userId/liked")
            .get()
            .await()

        FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/$userId/liked/${snapshot.childrenCount}")
            .setValue(movieId.toString())
            .await()

        emit(RemoteResult.Success(Unit))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteMovieFromFavorites(userId: String, movieId: Int) = flow {
        emit(RemoteResult.Loading)

        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/$userId/liked")
            .get()
            .await()

        val likedMovies = snapshot.value as? MutableList<String>

        likedMovies?.remove(movieId.toString())

        FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/$userId/liked/")
            .setValue(likedMovies)
            .await()

        emit(RemoteResult.Success(Unit))
    }.flowOn(Dispatchers.IO)

    override suspend fun addReview(review: Review, userId: String, movieId: Int) = flow {
        emit(RemoteResult.Loading)

        addReviewToMovie(review, userId, movieId)
        addMovieToUser(userId, movieId)

        emit(RemoteResult.Success(Unit))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteReview(review: Review, userId: String, movieId: Int) = flow {
        emit(RemoteResult.Loading)

        deleteUserReview(userId, movieId)
        deleteMovieFromUser(userId, movieId)

        emit(RemoteResult.Success(Unit))
    }.flowOn(Dispatchers.IO)

    private suspend fun addMovieToUser(userId: String, movieId: Int) {
        val database = FirebaseDatabase.getInstance().reference

        val snapshot = database
            .child("Users/$userId/reviewed")
            .get()
            .await()

        database
            .child("Users/$userId/reviewed/${snapshot.childrenCount}")
            .setValue(movieId.toString())
            .await()
    }

    private suspend fun deleteUserReview(userId: String, movieId: Int) {
        FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId/reviews/$userId")
            .setValue(null)
            .await()
    }

    private suspend fun deleteMovieFromUser(userId: String, movieId: Int) {
        val database = FirebaseDatabase.getInstance().reference

        val moviesIds = database
            .child("Users/$userId/reviewed")
            .get()
            .await()
            .value as MutableList<String>

        moviesIds.remove(movieId.toString())

        database
            .child("Users/$userId/reviewed")
            .setValue(moviesIds)
            .await()
    }

    private suspend fun addReviewToMovie(review: Review, userId: String, movieId: Int) {
        FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId/reviews/$userId")
            .setValue(review)
            .await()
    }
}