package com.stdio.mangoapp.data

import com.stdio.mangoapp.data.db.ProfileDao
import com.stdio.mangoapp.data.remote.BaseRepository
import com.stdio.mangoapp.data.remote.RemoteDataSource
import com.stdio.mangoapp.domain.models.CheckAuthCodeReq
import com.stdio.mangoapp.domain.models.ProfileData
import com.stdio.mangoapp.domain.models.RegisterRequest
import com.stdio.mangoapp.domain.models.SendPhoneRequest
import com.stdio.mangoapp.domain.models.UpdateProfileDataRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepository(
    private val remoteDataSource: RemoteDataSource,
    private val profileDao: ProfileDao
) : BaseRepository() {

    suspend fun sendAuthCode(phone: String) = flow {
        emit(apiCall { remoteDataSource.sendAuthCode(SendPhoneRequest(phone)) })
    }

    suspend fun checkAuthCode(phone: String, code: String) = flow {
        emit(apiCall { remoteDataSource.checkAuthCode(CheckAuthCodeReq(phone, code)) })
    }

    suspend fun register(phone: String, name: String, username: String) = flow {
        emit(apiCall { remoteDataSource.register(RegisterRequest(phone, name, username)) })
    }

    suspend fun getCurrentUser() = flow {
        emit(apiCall { remoteDataSource.getCurrentUser() })
    }

    suspend fun updateUser(body: UpdateProfileDataRequest) = flow {
        emit(apiCall { remoteDataSource.updateUser(body) })
    }

    // DATABASE

    val profileData: Flow<ProfileData?> = profileDao.getUserData()

    suspend fun insertProfileData(profileData: ProfileData) {
        profileDao.insert(profileData)
    }

    suspend fun updateProfileData(profileData: ProfileData) {
        profileDao.update(profileData)
    }
}