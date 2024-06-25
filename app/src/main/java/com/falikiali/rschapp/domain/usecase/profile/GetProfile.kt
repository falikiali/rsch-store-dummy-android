package com.falikiali.rschapp.domain.usecase.profile

import com.falikiali.rschapp.domain.model.Profile
import com.falikiali.rschapp.domain.repository.ProfileRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfile @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(): Flow<ResultState<Profile>> {
        return profileRepository.getProfile()
    }

}