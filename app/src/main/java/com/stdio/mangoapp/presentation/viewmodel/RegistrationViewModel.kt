package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.common.TokenManager
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class RegistrationViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DataState<LoginResponse>>(DataState.Initial)
    val uiState: StateFlow<DataState<LoginResponse>> = _uiState.asStateFlow()
    private val tokenManager: TokenManager by inject(TokenManager::class.java)
    private val token = MutableLiveData<String?>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.getAccessToken().collect {
                withContext(Dispatchers.Main) {
                    token.value = it
                }
            }
        }
    }

    fun register(phone: String, name: String, username: String) {
        _uiState.value = DataState.Loading
        viewModelScope.launch {
            repository.register(phone, name, username).collect {
                if (it is DataState.Success) {
                    saveAccessToken(it.data.accessToken)
                    saveRefreshToken(it.data.refreshToken)
                }
                _uiState.value = it
            }
        }
    }

    private fun saveAccessToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveAccessToken(token)
        }
    }

    private fun saveRefreshToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveRefreshToken(token)
        }
    }
}