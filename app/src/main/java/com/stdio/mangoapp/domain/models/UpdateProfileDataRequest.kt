package com.stdio.mangoapp.domain.models

data class UpdateProfileDataRequest(
    var name: String? = null,
    var username: String? = null,
    var birthday: String? = null,
    var city: String? = null,
    var vk: String? = null,
    var instagram: String? = null,
    var status: String? = null,
    var avatar: AvatarUploadingRequest? = null
)