package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class UpdateSelectedProductInCartRequestBody(
    val id: String,
    @field:SerializedName("is_selected") val isSelected: Boolean
)
