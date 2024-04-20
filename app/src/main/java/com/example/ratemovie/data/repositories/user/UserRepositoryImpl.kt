package com.example.ratemovie.data.repositories.user

import com.example.ratemovie.domain.entities.User
import com.example.ratemovie.domain.entities.LoginResult
import com.example.ratemovie.domain.entities.RegistrationResult
import com.example.ratemovie.domain.entities.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserRepositoryImpl : UserRepository {

    override suspend fun getUser(userId: String?): User? {
        val snapshot = FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/${userId}")
            .get()
            .await()

        return snapshot.getValue(User::class.java)
    }

    override suspend fun getUsername(userId: String): String {
        return FirebaseDatabase
            .getInstance()
            .reference
            .child("Users/$userId/username")
            .get()
            .await()
            .value as String
    }

    override suspend fun signIn(email: String, password: String): LoginResult {
        if (email.isEmpty() || password.isEmpty()) return LoginResult.Error.EmptyFields

        try {
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()
        } catch (e: Exception) {
            return when (e) {
                is FirebaseAuthInvalidCredentialsException -> LoginResult.Error.InvalidCredentials
                is FirebaseAuthInvalidUserException -> LoginResult.Error.InvalidCredentials
                else -> LoginResult.Error.Default
            }
        }

        val userId: String
        val user: User

        try {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
            user = getUser(userId)!!
        } catch (e: NullPointerException) {
            return LoginResult.Error.Default
        }

        return LoginResult.Success(user)
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override suspend fun signUp(
        username: String,
        email: String,
        password: String
    ): RegistrationResult {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) return RegistrationResult.Error.EmptyFields

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
            return when (e) {
                is FirebaseAuthWeakPasswordException -> RegistrationResult.Error.WeakPassword
                is FirebaseAuthInvalidCredentialsException -> RegistrationResult.Error.InvalidCredentials
                is FirebaseAuthUserCollisionException -> RegistrationResult.Error.EmailCollision
                else -> RegistrationResult.Error.Default
            }
        }

        val userId: String
        val user: User

        try {
            userId = FirebaseAuth.getInstance().currentUser!!.uid
            user = getUser(userId)!!
        } catch (e: NullPointerException) {
            return RegistrationResult.Error.Default
        }

        return RegistrationResult.Success(user)
    }

    override suspend fun addMovieToFavorites(userId: String, movieId: Int) {
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
    }

    override suspend fun deleteMovieFromFavorites(userId: String, movieId: Int) {
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
    }

    override suspend fun addReview(review: Review, userId: String, movieId: Int) {
        addReviewToMovie(review, userId, movieId)

        addMovieToUser(userId, movieId)
    }

    override suspend fun deleteReview(review: Review, userId: String, movieId: Int) {
        deleteUserReview(userId, movieId)

        deleteMovieFromUser(userId, movieId)
    }

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