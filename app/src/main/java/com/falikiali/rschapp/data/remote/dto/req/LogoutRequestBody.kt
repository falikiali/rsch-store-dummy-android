package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class LogoutRequestBody(
    @field:SerializedName("access_token") val accessToken: String
)
