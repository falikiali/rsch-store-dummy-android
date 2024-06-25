package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class UpdateProductInCartRequestBody(
    val id: String,
    @field:SerializedName("qty") val quantity: Int
)