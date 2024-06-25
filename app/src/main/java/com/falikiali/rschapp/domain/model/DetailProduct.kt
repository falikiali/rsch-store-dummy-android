package com.falikiali.rschapp.domain.model

data class DetailProduct(
    val category: String,
    val sizes: List<DetailSizeProduct>
)

data class DetailSizeProduct(
    val id: String,
    val size: String,
    val stock: Int
)
