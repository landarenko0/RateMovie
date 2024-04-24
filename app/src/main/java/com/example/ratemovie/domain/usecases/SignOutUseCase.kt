package com.example.ratemovie.domain.usecases

import com.example.ratemovie.data.repositories.user.UserRepository

class SignOutUseCase(private val userRepository: UserRepository) {

    operator fun invoke() {
        userRepository.signOut()
    }
}