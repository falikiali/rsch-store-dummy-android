package com.falikiali.rschapp.data

import android.app.Application
import com.falikiali.rschapp.R
import com.falikiali.rschapp.data.remote.ProfileApiService
import com.falikiali.rschapp.data.remote.dto.req.UpdatePasswordRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdatePhoneNumberRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdateProfileRequestBody
import com.falikiali.rschapp.data.remote.dto.res.BaseErrorResponse
import com.falikiali.rschapp.domain.model.Profile
import com.falikiali.rschapp.domain.model.UpdateProfile
import com.falikiali.rschapp.domain.repository.ProfileRepository
import com.falikiali.rschapp.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val profileApiService: ProfileApiService, private val application: Application): ProfileRepository {
    override suspend fun getProfile(): Flow<ResultState<Profile>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val response = profileApiService.getProfile()

                val data = Profile(
                    response.data.email,
                    response.data.fullname,
                    response.data.username,
                    response.data.phoneNumber
                )

                emit(ResultState.Success(data))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.data != null) {
                        emit(ResultState.Failed(err.data.toString(), err.statusCode))
                    } else {
                        emit(ResultState.Failed(err.statusMessage, err.statusCode))
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateProfile(
        fullname: String,
        username: String
    ): Flow<ResultState<UpdateProfile>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = UpdateProfileRequestBody(fullname, username)
                val response = profileApiService.updateProfile(body)

                val data = UpdateProfile(
                    response.data.fullname,
                    response.data.username
                )

                emit(ResultState.Success(data))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.update_profile_bad_request), err.statusCode))
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
            } catch (e: java.lang.Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updatePhoneNumber(phoneNumber: String): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = UpdatePhoneNumberRequestBody(phoneNumber)
                val response = profileApiService.updatePhoneNumber(body)

                emit(ResultState.Success(response.data.newPhoneNumber))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.update_phone_number_bad_request), err.statusCode))
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
            } catch (e: java.lang.Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = UpdatePasswordRequestBody(oldPassword, newPassword, confirmNewPassword)
                val response = profileApiService.updatePassword(body)

                emit(ResultState.Success(response.statusMessage))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.update_password_bad_request), err.statusCode))
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
            } catch (e: java.lang.Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

}