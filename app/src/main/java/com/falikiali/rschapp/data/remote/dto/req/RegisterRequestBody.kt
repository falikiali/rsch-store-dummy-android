package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    val email: String,
    val password: String,
    @field:SerializedName("confirm_password") val confirmPassword: String
)
