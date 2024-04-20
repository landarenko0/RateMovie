package com.example.ratemovie.domain.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitBuilder {
    private val contentType = "application/json".toMediaType()

    fun build(url: String): Retrofit {
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}