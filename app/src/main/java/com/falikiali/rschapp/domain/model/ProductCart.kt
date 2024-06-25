package com.falikiali.rschapp.domain.model

data class ProductCart(
    val id: String,
    val idProduct: String,
    val idProductSize: String,
    val productName: String,
    val productImage: String,
    val productSize: String,
    val productStock: Int,
    val isSelected: Boolean,
    val quantity: Int,
    val totalPrice: Int
)
