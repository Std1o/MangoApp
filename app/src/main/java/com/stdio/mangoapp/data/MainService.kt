package com.stdio.mangoapp.data

import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.RegisterRequest
import com.stdio.mangoapp.domain.models.SendPhoneRequest
import com.stdio.mangoapp.domain.models.SignInResponse
import com.stdio.mangoapp.domain.models.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainService {

    @POST("users/send-auth-code/")
    suspend fun sendAuthCode(@Body body: SendPhoneRequest): Response<SimpleResponse>

    @POST("users/check-auth-code/")
    suspend fun checkAuthCode(@Body body: CheckAuthCodeReq): Response<SignInResponse>

    @POST("users/register/")
    suspend fun register(@Body body: RegisterRequest): Response<SignInResponse>
}