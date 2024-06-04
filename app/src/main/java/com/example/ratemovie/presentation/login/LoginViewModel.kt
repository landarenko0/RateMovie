package com.example.ratemovie.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.SignInUseCase
import com.example.ratemovie.domain.remote.LoginResult
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<RemoteResult<LoginResult?>>(RemoteResult.Success(null))
    val loginResult: LiveData<RemoteResult<LoginResult?>> = _loginResult

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password).collect {
                _loginResult.value = it

                if (it is RemoteResult.Success && it.data is LoginResult.Success) {
                    Globals.User.value = it.data.user
                }
            }
        }
    }

    fun resetState() {
        _loginResult.value = RemoteResult.Success(null)
    }
}