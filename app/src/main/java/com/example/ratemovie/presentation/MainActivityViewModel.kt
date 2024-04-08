package com.example.ratemovie.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.UserRepositoryImpl
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.User
import com.example.ratemovie.domain.usecases.GetUserUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val user = MutableLiveData<User?>()

    private val userRepository = UserRepositoryImpl()

    private val getUserUseCase = GetUserUseCase(userRepository)

    init {
        val userId = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

        viewModelScope.launch {
            user.value = getUserUseCase(userId)
        }
    }

    fun addLikedMovie(movie: Movie) {
        val likedMovies = user.value!!.likedMovies.toMutableList()
        likedMovies.add(movie.id.toString())

        user.value = user.value?.copy(likedMovies = likedMovies)
    }

    fun deleteLikedMovie(movie: Movie) {
        val likedMovies = user.value!!.likedMovies.toMutableList()
        likedMovies.remove(movie.id.toString())

        user.value = user.value?.copy(likedMovies = likedMovies)
    }

    fun addReviewedMovie(movie: Movie) {
        val reviewedMovies = user.value!!.reviewedMovies.toMutableList()

        if (!reviewedMovies.contains(movie.id.toString())) {
            reviewedMovies.add(movie.id.toString())
        }

        user.value = user.value?.copy(reviewedMovies = reviewedMovies)
    }

    fun deleteReviewedMovie(movie: Movie) {
        val reviewedMovies = user.value!!.reviewedMovies.toMutableList()
        reviewedMovies.remove(movie.id.toString())

        user.value = user.value?.copy(reviewedMovies = reviewedMovies)
    }
}