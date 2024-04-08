package com.example.ratemovie.domain.entities

sealed class LoginResult {

    data class Success(val user: User) : LoginResult()

    sealed class Error : LoginResult() {

        data object EmptyFields : Error()

        data object InvalidCredentials : Error()

        data object Default : Error()
    }
}