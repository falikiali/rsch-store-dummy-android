package com.falikiali.rschapp.domain.usecase

import com.falikiali.rschapp.domain.usecase.auth.Login
import com.falikiali.rschapp.domain.usecase.auth.Logout
import com.falikiali.rschapp.domain.usecase.auth.Register
import javax.inject.Inject

data class AuthUseCase @Inject constructor(
    val login: Login,
    val register: Register,
    val logout: Logout
)
