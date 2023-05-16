package com.stdio.mangoapp.data

import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.RegisterRequest
import com.stdio.mangoapp.domain.models.SendPhoneRequest
import com.stdio.mangoapp.domain.models.LoginResponse
import com.stdio.mangoapp.domain.models.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MainService {

    @POST("users/send-auth-code/")
    suspend fun sendAuthCode(@Body body: SendPhoneRequest): Response<SimpleResponse>

    @POST("users/check-auth-code/")
    suspend fun checkAuthCode(@Body body: CheckAuthCodeReq): Response<LoginResponse>

    @POST("users/register/")
    suspend fun register(@Body body: RegisterRequest): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<LoginResponse>
}