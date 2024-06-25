package com.falikiali.rschapp.domain.usecase.product

import com.falikiali.rschapp.domain.model.DetailProduct
import com.falikiali.rschapp.domain.repository.ProductRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProduct @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(idProduct: String, idCategory: Int): Flow<ResultState<DetailProduct>> {
        return productRepository.getProduct(idProduct, idCategory)
    }

}