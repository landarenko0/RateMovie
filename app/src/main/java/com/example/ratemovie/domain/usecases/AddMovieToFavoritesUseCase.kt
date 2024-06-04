package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import javax.inject.Inject

class AddMovieToFavoritesUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke(
        userId: String,
        movieId: Int
    ) = repository.addMovieToFavorites(userId, movieId)
}