package com.example.ratemovie.domain.remote

import com.example.ratemovie.domain.entities.User

sealed class LoginResult {

    data class Success(val user: User) : LoginResult()

    sealed class Error : LoginResult() {

        companion object {
            const val EMPTY_FIELDS = "Empty fields"
            const val INVALID_CREDENTIALS = "Invalid credentials"
            const val DEFAULT = "Default"
        }
    }
}