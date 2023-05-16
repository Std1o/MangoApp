package com.stdio.mangoapp.data.remote

import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.RegisterRequest
import com.stdio.mangoapp.domain.models.SendPhoneRequest
import com.stdio.mangoapp.domain.models.UpdateProfileDataRequest

class RemoteDataSource(private val mainService: MainService) {

    suspend fun sendAuthCode(body: SendPhoneRequest) = mainService.sendAuthCode(body)

    suspend fun checkAuthCode(body: CheckAuthCodeReq) = mainService.checkAuthCode(body)

    suspend fun register(body: RegisterRequest) = mainService.register(body)

    suspend fun getCurrentUser() = mainService.getCurrentUser()

    suspend fun updateUser(body: UpdateProfileDataRequest) = mainService.updateUser(body)
}