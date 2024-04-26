package com.example.ratemovie.domain.entities

import com.google.firebase.auth.FirebaseAuth

data class User(
    val username: String,
    val email: String,
    val liked: MutableList<String>,
    val reviewed: MutableList<String>
) {
    val id
        get() = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw RuntimeException("User is null")

    constructor() : this("", "", mutableListOf(), mutableListOf())
    constructor(username: String, email: String) : this(
        username,
        email,
        mutableListOf(),
        mutableListOf()
    )

    fun addMovieToFavorites(movieId: String) = liked.add(movieId)

    fun deleteMovieFromFavorites(movieId: String) = liked.remove(movieId)

    fun addMovieToReviewed(movieId: String) = reviewed.add(movieId)

    fun deleteMovieFromReviewed(movieId: String) = reviewed.remove(movieId)
}