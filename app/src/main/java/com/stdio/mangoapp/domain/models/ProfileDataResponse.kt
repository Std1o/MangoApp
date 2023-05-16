package com.stdio.mangoapp.domain.models

import com.google.gson.annotations.SerializedName

data class ProfileDataResponse(@SerializedName("profile_data") val profileData: ProfileData) : Profile
