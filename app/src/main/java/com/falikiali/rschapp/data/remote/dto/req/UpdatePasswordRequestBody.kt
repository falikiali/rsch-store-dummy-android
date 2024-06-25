package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequestBody(
    @field:SerializedName("old_password") val oldPassword: String,
    @field:SerializedName("new_password") val newPassword: String,
    @field:SerializedName("confirm_new_password") val confirmNewPassword: String
)
