package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.models.ProfileDataResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: MainRepository) : BaseViewModel<ProfileDataResponse>() {

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            launchRequest(repository.getCurrentUser())
        }
    }
}