package com.falikiali.rschapp.data.remote

import com.falikiali.rschapp.data.remote.dto.req.UpdatePasswordRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdatePhoneNumberRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdateProfileRequestBody
import com.falikiali.rschapp.data.remote.dto.res.BaseResponse
import com.falikiali.rschapp.data.remote.dto.res.BaseResponseWithoutData
import com.falikiali.rschapp.data.remote.dto.res.ProfileResponse
import com.falikiali.rschapp.data.remote.dto.res.UpdatePhoneNumberResponse
import com.falikiali.rschapp.data.remote.dto.res.UpdateProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApiService {

    @GET("user")
    suspend fun getProfile(): BaseResponse<ProfileResponse>

    @PUT("user/profile")
    suspend fun updateProfile(@Body body: UpdateProfileRequestBody): BaseResponse<UpdateProfileResponse>

    @PUT("user/phone-number")
    suspend fun updatePhoneNumber(@Body body: UpdatePhoneNumberRequestBody): BaseResponse<UpdatePhoneNumberResponse>

    @PUT("user/password")
    suspend fun updatePassword(@Body body: UpdatePasswordRequestBody): BaseResponseWithoutData

}