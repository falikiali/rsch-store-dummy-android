package com.falikiali.rschapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val purpose: String,
    val category: Int,
    val stock: Int,
    val image: String,
) : Parcelable
