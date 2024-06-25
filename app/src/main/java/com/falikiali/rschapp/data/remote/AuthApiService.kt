package com.falikiali.rschapp.data.remote

import com.falikiali.rschapp.data.remote.dto.req.LoginRequestBody
import com.falikiali.rschapp.data.remote.dto.req.LogoutRequestBody
import com.falikiali.rschapp.data.remote.dto.req.RegisterRequestBody
import com.falikiali.rschapp.data.remote.dto.res.BaseResponse
import com.falikiali.rschapp.data.remote.dto.res.BaseResponseWithoutData
import com.falikiali.rschapp.data.remote.dto.res.LoginResponse
import com.falikiali.rschapp.data.remote.dto.res.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")
    suspend fun login(@Body body: LoginRequestBody): BaseResponse<LoginResponse>

    @POST("register")
    suspend fun register(@Body body: RegisterRequestBody): BaseResponse<RegisterResponse>

    @HTTP(method = "DELETE", path = "logout", hasBody = true)
    suspend fun logout(@Body body: LogoutRequestBody): BaseResponseWithoutData

}