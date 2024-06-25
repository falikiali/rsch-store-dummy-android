package com.falikiali.rschapp.domain.usecase.auth

import com.falikiali.rschapp.helper.ResultState
import com.falikiali.rschapp.domain.model.Auth
import com.falikiali.rschapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Register @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String, confirmPassword: String): Flow<ResultState<Auth>> {
        return authRepository.register(email, password, confirmPassword)
    }

}