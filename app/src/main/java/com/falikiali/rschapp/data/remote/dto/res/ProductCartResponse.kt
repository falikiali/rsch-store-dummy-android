package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class ProductCartResponse(
    val id: String,
    @field:SerializedName("id_product") val idProduct: String,
    @field:SerializedName("id_product_size") val idProductSize: String,
    @field:SerializedName("product_name") val productName: String,
    @field:SerializedName("product_image") val productImage: String,
    @field:SerializedName("product_size") val productSize: String,
    @field:SerializedName("product_stock") val productStock: Int,
    @field:SerializedName("is_selected") val isSelected: Boolean,
    @field:SerializedName("qty") val quantity: Int,
    @field:SerializedName("total_price") val totalPrice: Int
)
