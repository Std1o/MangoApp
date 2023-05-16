package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.ProfileData
import com.stdio.mangoapp.domain.models.ProfileDataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DataState<ProfileDataResponse>>(DataState.Initial)
    val uiState: StateFlow<DataState<ProfileDataResponse>> = _uiState.asStateFlow()
    val profileData: Flow<ProfileData?> = repository.profileData

    init {
        viewModelScope.launch {
            repository.profileData.collect {
                if (it == null) {
                    getCurrentUser()
                }
            }
        }
    }

    private fun getCurrentUser() {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            repository.getCurrentUser().collect {
                if (it is DataState.Success) {
                    repository.insertProfileData(it.data.profileData)
                }
                _uiState.value = it
            }
        }
    }
}