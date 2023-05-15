package com.stdio.mangoapp.data

import com.stdio.mangoapp.domain.models.SendPhoneRequest
import kotlinx.coroutines.flow.flow

class MainRepository(private val remoteDataSource: RemoteDataSource) : BaseRepository(){

    suspend fun sendAuthCode(phone: String) = flow {
        emit(apiCall { remoteDataSource.sendAuthCode(SendPhoneRequest(phone)) })
    }
}