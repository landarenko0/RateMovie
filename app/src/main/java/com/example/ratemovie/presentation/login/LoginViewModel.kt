package com.example.ratemovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.usecases.SignInUseCase
import com.example.ratemovie.domain.entities.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _shouldShowLoader = MutableLiveData<Boolean>()
    val shouldShowLoader: LiveData<Boolean> = _shouldShowLoader

    private val _shouldCloseFragment = MutableLiveData<Unit>()
    val shouldCloseFragment: LiveData<Unit> = _shouldCloseFragment

    private val userRepository = UserRepositoryImpl()

    private val signInUseCase = SignInUseCase(userRepository)

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _shouldShowLoader.value = true

            val result = signInUseCase(email, password)

            _loginResult.value = result

            _shouldShowLoader.value = false

            if (result is LoginResult.Success) _shouldCloseFragment.value = Unit
        }
    }
}