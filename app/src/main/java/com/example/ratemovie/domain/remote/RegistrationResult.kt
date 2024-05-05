package com.example.ratemovie.domain.remote

import com.example.ratemovie.domain.entities.User

sealed class RegistrationResult {

    data class Success(val user: User) : RegistrationResult()

    sealed class Error : RegistrationResult() {

        companion object {
            const val EMPTY_FIELDS = "Empty fields"
            const val INVALID_SYMBOLS = "Invalid symbols"
            const val WEAK_PASSWORD = "Weak password"
            const val INVALID_CREDENTIALS = "Invalid credentials"
            const val EMAIL_COLLISION = "Email collision"
            const val USERNAME_COLLISION = "Username collision"
            const val DEFAULT = "Default"
        }
    }
}