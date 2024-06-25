package com.falikiali.rschapp.data.remote

import com.falikiali.rschapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val preferencesRepository: PreferencesRepository): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = runBlocking { preferencesRepository.getAccessToken().first() }
        request.addHeader("Authorization", "Bearer $token")

        return chain.proceed(request.build())
    }

}