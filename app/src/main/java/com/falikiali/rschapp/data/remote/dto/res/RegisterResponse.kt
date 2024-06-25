package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("access_token") val accessToken: String
)
