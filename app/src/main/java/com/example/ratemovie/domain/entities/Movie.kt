package com.example.ratemovie.domain.entities

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class Movie(@SerialName("id") val id: Int,
                 @SerialName("name") val title: String,
                 @SerialName("rating") val rating: Rating,
                 @SerialName("description") val description: String,
                 @SerialName("poster") val posterUrl: Preview?,
                 @SerialName("genres") val genres: List<Genre>
) : Parcelable
