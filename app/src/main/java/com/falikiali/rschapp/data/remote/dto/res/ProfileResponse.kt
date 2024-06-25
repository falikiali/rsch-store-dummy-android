package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val email: String,
    val fullname: String,
    val username: String,
    @field:SerializedName("phone_number") val phoneNumber: String
)
