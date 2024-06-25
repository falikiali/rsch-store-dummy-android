package com.falikiali.rschapp.domain.usecase.preferences

import com.falikiali.rschapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccessToken @Inject constructor(private val preferencesRepository: PreferencesRepository) {

    suspend operator fun invoke(): Flow<String> {
        return preferencesRepository.getAccessToken()
    }

}