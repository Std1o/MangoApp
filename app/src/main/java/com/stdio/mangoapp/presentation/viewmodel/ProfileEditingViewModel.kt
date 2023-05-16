package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.models.Avatars
import com.stdio.mangoapp.domain.models.ProfileDataResponse
import com.stdio.mangoapp.domain.models.UpdateProfileDataRequest
import kotlinx.coroutines.launch

class ProfileEditingViewModel(private val repository: MainRepository) : BaseViewModel<Avatars>() {

    val userData = UpdateProfileDataRequest()

    fun updateUser() {
        viewModelScope.launch {
            launchRequest(repository.updateUser(userData))
        }
    }
}