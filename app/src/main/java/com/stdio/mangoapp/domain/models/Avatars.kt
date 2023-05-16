package com.stdio.mangoapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Avatars(val avatar: String, val bigAvatar: String, val miniAvatar: String) : Parcelable
