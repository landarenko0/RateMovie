package com.example.ratemovie.domain.entities

data class User(
    val username: String,
    val email: String,
    val liked: List<String>,
    val reviewed: List<String>
)
{
    constructor() : this("", "", emptyList(), emptyList())
    constructor(username: String, email: String) : this(username, email, emptyList(), emptyList())
}