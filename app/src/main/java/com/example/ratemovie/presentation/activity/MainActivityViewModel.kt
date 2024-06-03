package com.example.ratemovie.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.usecases.GetUserUseCase
import com.example.ratemovie.domain.utils.Globals
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    init {
        val userId = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid

        viewModelScope.launch {
            getUserUseCase(userId).collect {
                if (it is RemoteResult.Success) {
                    Globals.User.value = it.data
                }
            }
        }
    }
}