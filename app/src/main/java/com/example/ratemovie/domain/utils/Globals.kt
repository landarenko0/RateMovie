package com.example.ratemovie.domain.utils

import androidx.lifecycle.MutableLiveData
import com.example.ratemovie.domain.entities.User

object Globals {

    val User: MutableLiveData<User?> = MutableLiveData(null)
}