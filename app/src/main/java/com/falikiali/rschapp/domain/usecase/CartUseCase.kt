package com.falikiali.rschapp.domain.usecase

import com.falikiali.rschapp.domain.usecase.cart.AddProductToCart
import com.falikiali.rschapp.domain.usecase.cart.DeleteProductInCart
import com.falikiali.rschapp.domain.usecase.cart.GetProductInCart
import com.falikiali.rschapp.domain.usecase.cart.UpdateProductInCart
import com.falikiali.rschapp.domain.usecase.cart.UpdateSelectedProductInCart
import javax.inject.Inject

data class CartUseCase @Inject constructor(
    val addProductToCart: AddProductToCart,
    val deleteProductInCart: DeleteProductInCart,
    val updateProductInCart: UpdateProductInCart,
    val updateSelectedProductInCart: UpdateSelectedProductInCart,
    val getProductInCart: GetProductInCart
)