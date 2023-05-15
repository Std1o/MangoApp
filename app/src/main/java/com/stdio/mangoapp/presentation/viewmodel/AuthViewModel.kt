package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.CommonAuthData
import com.stdio.mangoapp.domain.usecases.CheckAuthCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: MainRepository,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DataState<CommonAuthData>>(DataState.Initial)
    val uiState: StateFlow<DataState<CommonAuthData>> = _uiState.asStateFlow()

    fun sendPhone(phone: String) {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            repository.sendAuthCode(phone).collect {
                _uiState.value = it
            }
        }
    }

    fun checkAuthCode(phone: String, code: String) {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            checkAuthCodeUseCase(phone, code).collect {
                _uiState.value = it
            }
        }
    }
}