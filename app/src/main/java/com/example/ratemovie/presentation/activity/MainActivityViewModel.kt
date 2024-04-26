package com.example.ratemovie.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.usecases.GetUserUseCase
import com.example.ratemovie.domain.utils.Globals
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val userRepository = UserRepositoryImpl()

    private val getUserUseCase = GetUserUseCase(userRepository)

    init {
        val userId = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

        viewModelScope.launch {
            Globals.User = getUserUseCase(userId)
        }
    }
}