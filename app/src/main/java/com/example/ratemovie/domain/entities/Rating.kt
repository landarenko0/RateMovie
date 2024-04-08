package com.example.ratemovie.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class Rating(@SerialName("kp") val kp: Float): Parcelable