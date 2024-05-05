package com.example.ratemovie.domain.remote

sealed class RemoteResult<out T> {

    data object Loading : RemoteResult<Nothing>()

    data class Success<out T>(val data: T) : RemoteResult<T>()

    data class Error(val message: String) : RemoteResult<Nothing>()
}