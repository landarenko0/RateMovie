package com.example.ratemovie.domain.entities

sealed class RegistrationResult {

    data class Success(val user: User) : RegistrationResult()

    sealed class Error : RegistrationResult() {

        data object EmptyFields : Error()

        data object InvalidSymbols : Error()

        data object WeakPassword : Error()

        data object InvalidCredentials : Error()

        data object EmailCollision : Error()

        data object UsernameCollision : Error()

        data object Default : Error()
    }
}