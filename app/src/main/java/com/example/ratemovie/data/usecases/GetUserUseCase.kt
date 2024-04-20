package com.example.ratemovie.data.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.entities.User

class GetUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(userId: String?): User? {
        return userRepository.getUser(userId)
    }
}