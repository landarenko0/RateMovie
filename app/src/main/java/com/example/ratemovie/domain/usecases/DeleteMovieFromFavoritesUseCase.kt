package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.UserRepository

class DeleteMovieFromFavoritesUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(
        userId: String,
        movieId: Int
    ) {
        repository.deleteMovieFromFavorites(userId, movieId)
    }
}