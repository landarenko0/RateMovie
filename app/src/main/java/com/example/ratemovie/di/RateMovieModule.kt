package com.example.ratemovie.di

import com.example.ratemovie.data.repositories.movie.MovieRepository
import com.example.ratemovie.data.repositories.movie.MovieRepositoryImpl
import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import com.example.ratemovie.data.repositories.movieslist.MoviesListRepositoryImpl
import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RateMovieModule {

    @Singleton
    @Provides
    fun provideMovieRepository(): MovieRepository {
        return MovieRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideMoviesListRepository(): MoviesListRepository {
        return MoviesListRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }
}