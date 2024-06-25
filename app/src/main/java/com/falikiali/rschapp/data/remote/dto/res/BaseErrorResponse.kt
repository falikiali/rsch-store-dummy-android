package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class BaseErrorResponse(
    @field:SerializedName("status_code") val statusCode: Int,
    @field:SerializedName("status_message") val statusMessage: String,
    @field:SerializedName("data") val data: Any? = null
)
