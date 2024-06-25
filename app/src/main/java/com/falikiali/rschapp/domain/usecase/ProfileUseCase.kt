package com.falikiali.rschapp.domain.usecase

import com.falikiali.rschapp.domain.usecase.profile.GetProfile
import com.falikiali.rschapp.domain.usecase.profile.UpdatePassword
import com.falikiali.rschapp.domain.usecase.profile.UpdatePhoneNumber
import com.falikiali.rschapp.domain.usecase.profile.UpdateProfile
import javax.inject.Inject

data class ProfileUseCase @Inject constructor(
    val getProfile: GetProfile,
    val updatePhoneNumber: UpdatePhoneNumber,
    val updateProfile: UpdateProfile,
    val updatePassword: UpdatePassword
)
