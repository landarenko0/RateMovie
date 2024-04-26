package com.example.ratemovie.domain.api

import com.example.ratemovie.domain.entities.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("docs")
    val movies: List<Movie>
)