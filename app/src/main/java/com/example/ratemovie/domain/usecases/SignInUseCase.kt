package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.entities.LoginResult
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): LoginResult {
        return userRepository.signIn(email, password)
    }
}