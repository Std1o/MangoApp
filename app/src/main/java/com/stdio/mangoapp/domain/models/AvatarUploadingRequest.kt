package com.stdio.mangoapp.domain.models

import com.google.gson.annotations.SerializedName

data class AvatarUploadingRequest(val filename: String, @SerializedName("base_64") val base64: String)
