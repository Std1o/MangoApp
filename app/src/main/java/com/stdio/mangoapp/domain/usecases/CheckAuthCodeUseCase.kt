package com.stdio.mangoapp.domain.usecases

import androidx.core.text.isDigitsOnly
import com.stdio.mangoapp.R
import com.stdio.mangoapp.data.MainRepository
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckAuthCodeUseCase(private val repository: MainRepository) {

    suspend operator fun invoke(phone: String, code: String): Flow<DataState<LoginResponse>> {
        return if (code.length == 6 && code.isDigitsOnly()) {
            repository.checkAuthCode(phone, code)
        } else {
            flow { emit(DataState.ValidationError(R.string.invalid_sms_code)) }
        }
    }
}