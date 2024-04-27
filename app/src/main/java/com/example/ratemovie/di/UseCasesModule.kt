package com.example.ratemovie.di

import com.example.ratemovie.data.repositories.movie.MovieRepository
import com.example.ratemovie.data.repositories.movieslist.MoviesListRepository
import com.example.ratemovie.data.repositories.user.UserRepository
import com.example.ratemovie.domain.usecases.AddMovieToFavoritesUseCase
import com.example.ratemovie.domain.usecases.AddReviewUseCase
import com.example.ratemovie.domain.usecases.CheckUserLikesMovieUseCase
import com.example.ratemovie.domain.usecases.DeleteMovieFromFavoritesUseCase
import com.example.ratemovie.domain.usecases.DeleteReviewUseCase
import com.example.ratemovie.domain.usecases.GetMovieReviewsUseCase
import com.example.ratemovie.domain.usecases.GetMoviesByIdsUseCase
import com.example.ratemovie.domain.usecases.GetNewMoviesListUseCase
import com.example.ratemovie.domain.usecases.GetUserReviewUseCase
import com.example.ratemovie.domain.usecases.GetUserUseCase
import com.example.ratemovie.domain.usecases.GetUsernameUseCase
import com.example.ratemovie.domain.usecases.SearchMoviesByNameUseCase
import com.example.ratemovie.domain.usecases.SignInUseCase
import com.example.ratemovie.domain.usecases.SignOutUseCase
import com.example.ratemovie.domain.usecases.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Singleton
    @Provides
    fun provideAddMovieToFavoritesUseCase(repository: UserRepository): AddMovieToFavoritesUseCase {
        return AddMovieToFavoritesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideAddReviewUseCase(repository: UserRepository): AddReviewUseCase {
        return AddReviewUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideCheckUserLikesMovieUseCase(repository: MovieRepository): CheckUserLikesMovieUseCase {
        return CheckUserLikesMovieUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteMovieFromFavoritesUseCase(repository: UserRepository): DeleteMovieFromFavoritesUseCase {
        return DeleteMovieFromFavoritesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteReviewUseCase(repository: UserRepository): DeleteReviewUseCase {
        return DeleteReviewUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetMovieReviewsUseCase(repository: MovieRepository): GetMovieReviewsUseCase {
        return GetMovieReviewsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetMoviesByIdsUseCase(repository: MoviesListRepository): GetMoviesByIdsUseCase {
        return GetMoviesByIdsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetNewMoviesListUseCase(repository: MoviesListRepository): GetNewMoviesListUseCase {
        return GetNewMoviesListUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetUsernameUseCase(repository: UserRepository): GetUsernameUseCase {
        return GetUsernameUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetUserReviewUseCase(repository: MovieRepository): GetUserReviewUseCase {
        return GetUserReviewUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSearchMoviesByNameUseCase(repository: MoviesListRepository): SearchMoviesByNameUseCase {
        return SearchMoviesByNameUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignInUseCase(repository: UserRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignOutUseCase(repository: UserRepository): SignOutUseCase {
        return SignOutUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideSignUpUseCase(repository: UserRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }
}