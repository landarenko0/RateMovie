package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.UserRepository

class GetUsernameUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(userId: String): String {
        return repository.getUsername(userId)
    }
}