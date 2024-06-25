package com.falikiali.rschapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.falikiali.rschapp.domain.repository.PreferencesRepository
import com.falikiali.rschapp.helper.ConstantData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>): PreferencesRepository {
    override suspend fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ConstantData.ACCESS_TOKEN]?.isNotBlank() ?: false
        }
    }

    override suspend fun getAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ConstantData.ACCESS_TOKEN] ?: ""
        }
    }

    override suspend fun setAccessToken(value: String) {
        dataStore.edit { preferences ->
            preferences[ConstantData.ACCESS_TOKEN] = value
        }
    }

    override suspend fun clearData() {
        dataStore.edit {
            it.remove(ConstantData.ACCESS_TOKEN)
        }
    }
}