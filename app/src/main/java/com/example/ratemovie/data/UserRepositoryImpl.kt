package com.example.ratemovie.data

import com.example.ratemovie.domain.repositories.UserRepository
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
        val snapshot = getMovieSnapshot(movieId)

        val oldRating = snapshot.child("rating").getValue(Float::class.java) ?: 0f
        val reviewsCount = snapshot.child("reviews").childrenCount

        val newRating: Float = calculateRating(oldRating, review.grade, reviewsCount)

        updateMovieRating(newRating, movieId)

        addReviewToMovie(review, userId, movieId)

        addMovieToUser(userId, movieId)
    }

    override suspend fun deleteReview(review: Review, userId: String, movieId: Int) {
        val snapshot = getMovieSnapshot(movieId)

        if (snapshot.child("reviews").childrenCount == 1L) {
            deleteMovieData(movieId)
        } else {
            val userGrade = review.grade
            val oldMovieRating = snapshot.child("rating").getValue(Float::class.java) ?: 0f
            val reviewsCount = snapshot.child("reviews").childrenCount

            val newRating = recalculateRatingWithoutUserGrade(
                userGrade,
                oldMovieRating,
                reviewsCount
            )

            updateMovieRating(newRating, movieId)
            deleteUserReview(userId, movieId)
        }

        deleteMovieFromUser(userId, movieId)
    }

    override suspend fun editReview(
        oldReview: Review,
        newReview: Review,
        userId: String,
        movieId: Int
    ) {
        val snapshot = getMovieSnapshot(movieId)

        val oldUserGrade = oldReview.grade
        val newUserGrade = newReview.grade
        val oldMovieRating = snapshot.child("rating").getValue(Float::class.java) ?: 0f
        val reviewsCount = snapshot.child("reviews").childrenCount

        val newMovieRating = recalculateRating(
            oldUserGrade,
            newUserGrade,
            oldMovieRating,
            reviewsCount
        )

        updateMovieRating(newMovieRating, movieId)

        addReviewToMovie(newReview, userId, movieId)
    }

    private suspend fun addMovieToUser(userId: String, movieId: Int) {
        val database = FirebaseDatabase.getInstance().reference

        val snapshot = database
            .child("Users/$userId/reviewedMovies")
            .get()
            .await()

        database
            .child("Users/$userId/reviewedMovies/${snapshot.childrenCount}")
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
            .child("Users/$userId/reviewedMovies")
            .get()
            .await()
            .value as MutableList<String>

        moviesIds.remove(movieId.toString())

        database
            .child("Users/$userId/reviewedMovies")
            .setValue(moviesIds)
            .await()
    }

    private suspend fun getMovieSnapshot(movieId: Int) =
        FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId")
            .get()
            .await()

    private fun calculateRating(oldRating: Float, userGrade: Float, reviewsCount: Long): Float {
        val rawRating = (oldRating * reviewsCount + userGrade) / (reviewsCount + 1)

        return "%.1f".format(rawRating).replace(',', '.').toFloat()
    }

    private suspend fun updateMovieRating(rating: Float, movieId: Int) {
        FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId/rating")
            .setValue(rating)
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

    private suspend fun deleteMovieData(movieId: Int) {
        FirebaseDatabase
            .getInstance()
            .reference
            .child("Movies/$movieId")
            .setValue(null)
            .await()
    }

    private fun recalculateRatingWithoutUserGrade(
        userGrade: Float,
        oldMovieRating: Float,
        reviewsCount: Long
    ): Float {
        val rawRating = (oldMovieRating * reviewsCount - userGrade) / (reviewsCount - 1)

        return "%.1f".format(rawRating).replace(',', '.').toFloat()
    }

    private fun recalculateRating(
        oldUserGrade: Float,
        newUserGrade: Float,
        oldMovieRating: Float,
        reviewsCount: Long
    ): Float {
        val difference = newUserGrade - oldUserGrade

        val rawRating = (oldMovieRating * reviewsCount + difference) / reviewsCount

        return "%.1f".format(rawRating).replace(',', '.').toFloat()
    }
}