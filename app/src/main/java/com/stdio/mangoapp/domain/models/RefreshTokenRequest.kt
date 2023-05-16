package com.stdio.mangoapp.domain.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(@SerializedName("refresh_token") val refreshToken: String)
