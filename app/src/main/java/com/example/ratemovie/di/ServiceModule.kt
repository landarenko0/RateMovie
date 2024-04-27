package com.example.ratemovie.di

import com.example.ratemovie.domain.services.MoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideMoviesApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }
}