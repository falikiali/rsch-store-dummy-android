package com.falikiali.rschapp.domain.usecase.cart

import com.falikiali.rschapp.domain.repository.CartRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteProductInCart @Inject constructor(private val cartRepository: CartRepository) {

    suspend operator fun invoke(ids: List<String>): Flow<ResultState<String>> {
        return cartRepository.deleteProductInCart(ids)
    }

}