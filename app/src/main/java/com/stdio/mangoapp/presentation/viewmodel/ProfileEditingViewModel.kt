package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.Avatars
import com.stdio.mangoapp.domain.models.UpdateProfileDataRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileEditingViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DataState<Avatars>>(DataState.Initial)
    val uiState: StateFlow<DataState<Avatars>> = _uiState.asStateFlow()
    val userData = UpdateProfileDataRequest()

    fun updateUser() {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            repository.updateUser(userData).collect {
                if (it is DataState.Success) {
                    getCurrentUserAndSave()
                }
                _uiState.value = it
            }
        }
    }

    private fun getCurrentUserAndSave() {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            repository.getCurrentUser().collect {
                if (it is DataState.Success) {
                    repository.insertProfileData(it.data.profileData)
                }
            }
        }
    }
}