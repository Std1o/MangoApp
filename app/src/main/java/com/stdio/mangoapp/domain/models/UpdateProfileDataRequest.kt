package com.stdio.mangoapp.domain.models

data class UpdateProfileDataRequest(
    val name: String,
    val birthday: String,
    val city: String,
    val vk: String,
    val instagram: String,
    val status: String,
    val avatar: AvatarUploadingRequest
)