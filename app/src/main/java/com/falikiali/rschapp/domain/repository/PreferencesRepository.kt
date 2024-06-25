package com.falikiali.rschapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun isLoggedIn(): Flow<Boolean>
    suspend fun getAccessToken(): Flow<String>
    suspend fun setAccessToken(value: String)
    suspend fun clearData()

}