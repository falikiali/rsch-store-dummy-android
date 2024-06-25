package com.falikiali.rschapp.domain.usecase.profile

import com.falikiali.rschapp.domain.model.UpdateProfile
import com.falikiali.rschapp.domain.repository.ProfileRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProfile @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(fullname: String, username: String): Flow<ResultState<UpdateProfile>> {
        return profileRepository.updateProfile(fullname, username)
    }

}