package com.example.ratemovie.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.data.repositories.user.UserRepositoryImpl
import com.example.ratemovie.domain.usecases.SignUpUseCase
import com.example.ratemovie.domain.entities.RegistrationResult
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val loginResult: LiveData<RegistrationResult> = _registrationResult

    private val userRepository = UserRepositoryImpl()

    private val signUpUseCase = SignUpUseCase(userRepository)

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationResult.value = signUpUseCase(username, email, password)
        }
    }
}