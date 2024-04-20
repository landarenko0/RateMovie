package com.example.ratemovie.presentation.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.User
import com.example.ratemovie.data.usecases.GetUserUseCase
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

    fun addMovieToFavorites(movie: Movie) {
        val likedMovies = user.value!!.liked.toMutableList()
        likedMovies.add(movie.id.toString())

        user.value = user.value?.copy(liked = likedMovies)
    }

    fun deleteMovieFromFavorites(movie: Movie) {
        val likedMovies = user.value!!.liked.toMutableList()
        likedMovies.remove(movie.id.toString())

        user.value = user.value?.copy(liked = likedMovies)
    }

    fun addReviewedMovie(movie: Movie) {
        val reviewedMovies = user.value!!.reviewed.toMutableList()

        if (!reviewedMovies.contains(movie.id.toString())) {
            reviewedMovies.add(movie.id.toString())
        }

        user.value = user.value?.copy(reviewed = reviewedMovies)
    }

    fun deleteReviewedMovie(movie: Movie) {
        val reviewedMovies = user.value!!.reviewed.toMutableList()
        reviewedMovies.remove(movie.id.toString())

        user.value = user.value?.copy(reviewed = reviewedMovies)
    }
}