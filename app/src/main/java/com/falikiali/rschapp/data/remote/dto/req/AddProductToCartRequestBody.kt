package com.falikiali.rschapp.data.remote.dto.req

import com.google.gson.annotations.SerializedName

data class AddProductToCartRequestBody(
    @field:SerializedName("id_product") val idProduct: String,
    @field:SerializedName("id_product_size") val idProductSize: String,
    @field:SerializedName("qty") val quantity: Int
)
