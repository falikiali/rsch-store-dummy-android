package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class UpdatePhoneNumberResponse(
    @field:SerializedName("new_phone_number") val newPhoneNumber: String
)
