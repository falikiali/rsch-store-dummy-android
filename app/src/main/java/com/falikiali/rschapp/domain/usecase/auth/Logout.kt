package com.falikiali.rschapp.domain.usecase.auth

import com.falikiali.rschapp.domain.repository.AuthRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Logout @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(): Flow<ResultState<String>> {
        return authRepository.logout()
    }

}