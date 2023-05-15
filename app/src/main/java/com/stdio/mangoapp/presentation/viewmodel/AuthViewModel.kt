package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.SimpleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DataState<SimpleResponse>>(DataState.Initial)
    val uiState: StateFlow<DataState<SimpleResponse>> = _uiState.asStateFlow()

    fun sendPhone(phone: String) {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            repository.sendAuthCode(phone).collect {
                _uiState.value = it
            }
        }
    }
}