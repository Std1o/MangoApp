package com.stdio.mangoapp.data.remote

import com.stdio.mangoapp.domain.models.Avatars
import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.RegisterRequest
import com.stdio.mangoapp.domain.models.SendPhoneRequest
import com.stdio.mangoapp.domain.models.LoginResponse
import com.stdio.mangoapp.domain.models.UpdateProfileDataRequest
import com.stdio.mangoapp.domain.models.ProfileDataResponse
import com.stdio.mangoapp.domain.models.RefreshTokenRequest
import com.stdio.mangoapp.domain.models.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MainService {

    @POST("users/send-auth-code/")
    suspend fun sendAuthCode(@Body body: SendPhoneRequest): Response<SimpleResponse>

    @POST("users/check-auth-code/")
    suspend fun checkAuthCode(@Body body: CheckAuthCodeReq): Response<LoginResponse>

    @POST("users/register/")
    suspend fun register(@Body body: RegisterRequest): Response<LoginResponse>

    @POST("users/refresh-token/")
    suspend fun refreshToken(@Body body: RefreshTokenRequest): Response<LoginResponse>

    @GET("users/me/")
    suspend fun getCurrentUser(): Response<ProfileDataResponse>

    @PUT("users/me/")
    suspend fun updateUser(@Body body: UpdateProfileDataRequest): Response<Avatars>
}