package com.stdio.mangoapp.domain.models

import com.google.gson.annotations.SerializedName

data class SimpleResponse(@SerializedName("is_success") val isSuccess: Boolean)
