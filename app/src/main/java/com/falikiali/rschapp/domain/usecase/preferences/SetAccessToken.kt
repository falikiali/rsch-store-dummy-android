package com.falikiali.rschapp.domain.usecase.preferences

import com.falikiali.rschapp.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetAccessToken @Inject constructor(private val preferencesRepository: PreferencesRepository) {

    suspend operator fun invoke(value: String) {
        preferencesRepository.setAccessToken(value)
    }

}