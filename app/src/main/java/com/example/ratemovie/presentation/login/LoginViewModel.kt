package com.example.ratemovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.data.usecases.SignInUseCase
import com.example.ratemovie.domain.entities.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val userRepository = UserRepositoryImpl()

    private val signInUseCase = SignInUseCase(userRepository)

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = signInUseCase(email, password)
        }
    }
}