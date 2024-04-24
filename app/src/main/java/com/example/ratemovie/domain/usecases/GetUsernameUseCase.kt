package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository

class GetUsernameUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(userId: String): String {
        return repository.getUsername(userId)
    }
}