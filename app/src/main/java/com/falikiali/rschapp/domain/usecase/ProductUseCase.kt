package com.falikiali.rschapp.domain.usecase

import com.falikiali.rschapp.domain.usecase.product.GetProduct
import com.falikiali.rschapp.domain.usecase.product.GetProducts
import javax.inject.Inject

data class ProductUseCase @Inject constructor(
    val getProduct: GetProduct,
    val getProducts: GetProducts
)
