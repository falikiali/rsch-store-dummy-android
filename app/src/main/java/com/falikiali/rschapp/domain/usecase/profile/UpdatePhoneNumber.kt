package com.falikiali.rschapp.domain.usecase.profile

import com.falikiali.rschapp.domain.repository.ProfileRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePhoneNumber @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(phoneNumber: String): Flow<ResultState<String>> {
        return profileRepository.updatePhoneNumber(phoneNumber)
    }

}