package com.example.ratemovie.domain.entities

import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class Review(
    val text: String,
    val grade: Int,
    val username: String,
) : Parcelable {
    constructor() : this("", 0, "")
}
