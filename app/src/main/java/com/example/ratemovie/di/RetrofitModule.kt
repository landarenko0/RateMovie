package com.example.ratemovie.di

import com.example.ratemovie.domain.api.Api
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-API-KEY", Api.API_KEY)
                    .build()

                chain.proceed(request)
            }
            .build()
    }
}