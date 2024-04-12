package com.example.ratemovie.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.UserRepositoryImpl
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.User
import com.example.ratemovie.domain.usecases.GetUserUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val user = MutableLiveData<User?>()

    private val userRepository = UserRepositoryImpl()

    private val getUserUseCase = GetUserUseCase(userRepository)

    init {
        val userId = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

        viewModelScope.launch {
            user.value = getUserUseCase(userId)
        }
    }
}