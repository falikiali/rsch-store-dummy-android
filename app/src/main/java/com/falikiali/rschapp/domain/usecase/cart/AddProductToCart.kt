package com.falikiali.rschapp.domain.usecase.cart

import com.falikiali.rschapp.domain.repository.CartRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddProductToCart @Inject constructor(private val cartRepository: CartRepository) {

    suspend operator fun invoke(idProduct: String, idProductSize: String): Flow<ResultState<String>> {
        return cartRepository.addProductToCart(idProduct, idProductSize, 1)
    }

}