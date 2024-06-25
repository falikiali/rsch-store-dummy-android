package com.falikiali.rschapp.domain.usecase

import com.falikiali.rschapp.domain.usecase.preferences.ClearData
import com.falikiali.rschapp.domain.usecase.preferences.GetAccessToken
import com.falikiali.rschapp.domain.usecase.preferences.IsLoggedIn
import com.falikiali.rschapp.domain.usecase.preferences.SetAccessToken
import javax.inject.Inject

data class PreferencesUseCase @Inject constructor(
    val isLoggedIn: IsLoggedIn,
    val getAccessToken: GetAccessToken,
    val setAccessToken: SetAccessToken,
    val clearData: ClearData
)
