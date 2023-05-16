package com.stdio.mangoapp.data

import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.RegisterRequest
import com.stdio.mangoapp.domain.models.SendPhoneRequest

class RemoteDataSource(private val mainService: MainService) {

    suspend fun sendAuthCode(body: SendPhoneRequest) = mainService.sendAuthCode(body)

    suspend fun checkAuthCode(body: CheckAuthCodeReq) = mainService.checkAuthCode(body)

    suspend fun register(body: RegisterRequest) = mainService.register(body)
}