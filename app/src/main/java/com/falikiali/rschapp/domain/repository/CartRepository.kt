package com.falikiali.rschapp.domain.repository

import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun addProductToCart(idProduct: String, idProductSize: String, quantity: Int): Flow<ResultState<String>>
    suspend fun updateProductInCart(ids: List<String>, quantities: List<Int>): Flow<ResultState<String>>
    suspend fun updateSelectedProductInCart(idCart: String, isSelected: Boolean): Flow<ResultState<String>>
    suspend fun deleteProductInCart(ids: List<String>): Flow<ResultState<String>>
    suspend fun getProductInCart(): Flow<ResultState<List<ProductCart>>>

}