package com.example.ratemovie.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MovieDetailsViewModelFactory(private val movieId: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return MovieDetailsViewModel(
                movieId = movieId
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}