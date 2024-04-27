package com.example.ratemovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.SignInUseCase
import com.example.ratemovie.domain.entities.LoginResult
import com.example.ratemovie.domain.utils.Globals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _shouldShowLoader = MutableLiveData<Boolean>()
    val shouldShowLoader: LiveData<Boolean> = _shouldShowLoader

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _shouldShowLoader.value = true

            val result = signInUseCase(email, password)

            _loginResult.value = result

            _shouldShowLoader.value = false

            if (result is LoginResult.Success) Globals.User = result.user
        }
    }
}