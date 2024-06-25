package com.falikiali.rschapp.data

import android.app.Application
import com.falikiali.rschapp.R
import com.falikiali.rschapp.helper.ResultState
import com.falikiali.rschapp.data.remote.AuthApiService
import com.falikiali.rschapp.data.remote.dto.req.LoginRequestBody
import com.falikiali.rschapp.data.remote.dto.req.LogoutRequestBody
import com.falikiali.rschapp.data.remote.dto.req.RegisterRequestBody
import com.falikiali.rschapp.data.remote.dto.res.BaseErrorResponse
import com.falikiali.rschapp.domain.model.Auth
import com.falikiali.rschapp.domain.repository.AuthRepository
import com.falikiali.rschapp.domain.repository.PreferencesRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApiService: AuthApiService, private val application: Application, private val preferencesRepository: PreferencesRepository): AuthRepository {
    override suspend fun login(email: String, password: String): Flow<ResultState<Auth>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = LoginRequestBody(email, password)
                val response = authApiService.login(body)

                val data = Auth(response.data.accessToken)
                emit(ResultState.Success(data))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.login_bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data.toString(), err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun register(email: String, password: String, confirmPassword: String): Flow<ResultState<Auth>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = RegisterRequestBody(email, password, confirmPassword)
                val response = authApiService.register(body)

                val data = Auth(response.data.accessToken)
                emit(ResultState.Success(data))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.register_bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data as String, err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun logout(): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = LogoutRequestBody(preferencesRepository.getAccessToken().first())
                val response = authApiService.logout(body)

                emit(ResultState.Success(response.statusMessage))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.logout_bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data as String, err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }
}