package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class UpdatePhoneNumberRequestBody(
    @field:SerializedName("phone_number") val phoneNumber: String
)
