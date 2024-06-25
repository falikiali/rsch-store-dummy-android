package com.falikiali.rschapp.domain.repository

import com.falikiali.rschapp.helper.ResultState
import com.falikiali.rschapp.domain.model.Auth
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String): Flow<ResultState<Auth>>
    suspend fun register(email: String, password: String, confirmPassword: String): Flow<ResultState<Auth>>
    suspend fun logout(): Flow<ResultState<String>>

}