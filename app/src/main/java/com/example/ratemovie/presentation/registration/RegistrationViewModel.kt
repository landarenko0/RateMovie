package com.example.ratemovie.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.usecases.SignUpUseCase
import com.example.ratemovie.domain.entities.RegistrationResult
import com.example.ratemovie.domain.utils.Globals
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val loginResult: LiveData<RegistrationResult> = _registrationResult

    private val _shouldShowLoader = MutableLiveData<Boolean>()
    val shouldShowLoader: LiveData<Boolean> = _shouldShowLoader

    private val userRepository = UserRepositoryImpl()

    private val signUpUseCase = SignUpUseCase(userRepository)

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _shouldShowLoader.value = true

            val result = signUpUseCase(username, email, password)
            _registrationResult.value = result

            _shouldShowLoader.value = false

            if (result is RegistrationResult.Success) Globals.User = result.user
        }
    }
}