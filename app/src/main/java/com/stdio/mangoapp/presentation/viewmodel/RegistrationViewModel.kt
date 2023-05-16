package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.models.SignInResponse
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: MainRepository) : BaseViewModel<SignInResponse>() {

    fun register(phone: String, name: String, username: String) {
        viewModelScope.launch {
            launchRequest(repository.register(phone, name, username))
        }
    }
}