package com.stdio.mangoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stdio.mangoapp.common.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

open class BaseAuthViewModel : ViewModel(){

    private val tokenManager: TokenManager by KoinJavaComponent.inject(TokenManager::class.java)

    protected fun saveAccessToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveAccessToken(token)
        }
    }

    protected fun saveRefreshToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveRefreshToken(token)
        }
    }
}