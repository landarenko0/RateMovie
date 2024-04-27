package com.example.ratemovie.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("docs")
    val movies: List<Movie>
)