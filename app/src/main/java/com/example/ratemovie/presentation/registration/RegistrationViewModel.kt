package com.example.ratemovie.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.usecases.SignUpUseCase
import com.example.ratemovie.domain.remote.RegistrationResult
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _registrationResult = MutableLiveData<RemoteResult<RegistrationResult>>()
    val registrationResult: LiveData<RemoteResult<RegistrationResult>> = _registrationResult

    fun signUp(username: String, email: String, password: String) {
        viewModelScope.launch {
            signUpUseCase(username, email, password).collect {
                _registrationResult.value = it

                if (it is RemoteResult.Success) {
                    val result = it.data

                    if (result is RegistrationResult.Success) Globals.User.value = result.user
                }
            }
        }
    }
}