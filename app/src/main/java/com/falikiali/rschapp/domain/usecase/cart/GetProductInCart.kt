package com.falikiali.rschapp.domain.usecase.cart

import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.domain.repository.CartRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductInCart @Inject constructor(private val cartRepository: CartRepository) {

    suspend operator fun invoke(): Flow<ResultState<List<ProductCart>>> {
        return cartRepository.getProductInCart()
    }

}