package com.falikiali.rschapp.domain.usecase.profile

import com.falikiali.rschapp.domain.repository.ProfileRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePassword @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(oldPassword: String, newPassword: String, confirmNewPassword: String): Flow<ResultState<String>> {
        return profileRepository.updatePassword(oldPassword, newPassword, confirmNewPassword)
    }

}