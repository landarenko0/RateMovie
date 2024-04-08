package com.example.ratemovie.domain.usecases

import com.example.ratemovie.domain.repositories.UserRepository
import com.example.ratemovie.domain.entities.LoginResult

class SignInUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): LoginResult {
        return userRepository.signIn(email, password)
    }
}