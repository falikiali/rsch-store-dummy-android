package com.falikiali.rschapp.domain.usecase.preferences

import com.falikiali.rschapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsLoggedIn @Inject constructor(private val preferencesRepository: PreferencesRepository) {

    suspend operator fun invoke(): Flow<Boolean> {
        return preferencesRepository.isLoggedIn()
    }

}