package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.remote.LoginResult
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String) =
        userRepository.signIn(email, password)
}