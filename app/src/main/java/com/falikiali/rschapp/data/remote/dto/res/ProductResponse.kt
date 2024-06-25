package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val purpose: String,
    val category: Int,
    val stock: Int,
    val image: String,
    @field:SerializedName("detail_size") val detailSize: List<DetailSizeProductResponse>? = null
)

data class DetailSizeProductResponse(
    val id: String,
    val size: String,
    val stock: Int
)
