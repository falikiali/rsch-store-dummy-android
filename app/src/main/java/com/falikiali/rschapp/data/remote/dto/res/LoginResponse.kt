package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("access_token") val accessToken: String
)
