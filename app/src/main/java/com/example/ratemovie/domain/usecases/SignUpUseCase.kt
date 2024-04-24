package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.entities.RegistrationResult

class SignUpUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): RegistrationResult {
        return userRepository.signUp(username, email, password)
    }
}