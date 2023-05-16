package com.stdio.mangoapp.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ProfileData(
    val name: String,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: String?,
    @PrimaryKey val id: Int,
    val last: String?,
    val online: Boolean,
    val created: String,
    val phone: String,
    val completed_task: Int?,
) : Parcelable {
    @Ignore val avatars: Avatars? = null
}