package com.stdio.mangoapp.data

import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.SendPhoneRequest

class RemoteDataSource(private val mainService: MainService) {

    suspend fun sendAuthCode(phone: SendPhoneRequest) = mainService.sendAuthCode(phone)

    suspend fun checkAuthCode(params: CheckAuthCodeReq) = mainService.checkAuthCode(params)
}