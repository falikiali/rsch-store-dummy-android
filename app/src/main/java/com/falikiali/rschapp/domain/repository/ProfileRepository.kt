package com.falikiali.rschapp.domain.repository

import com.falikiali.rschapp.domain.model.Profile
import com.falikiali.rschapp.domain.model.UpdateProfile
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getProfile(): Flow<ResultState<Profile>>
    suspend fun updateProfile(fullname: String, username: String): Flow<ResultState<UpdateProfile>>
    suspend fun updatePhoneNumber(phoneNumber: String): Flow<ResultState<String>>
    suspend fun updatePassword(oldPassword: String, newPassword: String, confirmNewPassword: String): Flow<ResultState<String>>

}