package com.example.ratemovie.data.usecases

import com.example.ratemovie.data.repositories.user.UserRepository

class AddMovieToFavoritesUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(
        userId: String,
        movieId: Int
    ) {
        repository.addMovieToFavorites(userId, movieId)
    }
}