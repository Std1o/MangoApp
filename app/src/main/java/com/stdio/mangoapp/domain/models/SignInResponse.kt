package com.stdio.mangoapp.domain.models

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("is_user_exists") val isUserExist: Boolean?
) : CommonAuthData