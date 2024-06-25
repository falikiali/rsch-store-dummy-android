package com.falikiali.rschapp.domain.repository

import androidx.paging.PagingData
import com.falikiali.rschapp.domain.model.DetailProduct
import com.falikiali.rschapp.domain.model.Product
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProduct(idProduct: String, idCategory: Int): Flow<ResultState<DetailProduct>>
    fun getProducts(search: String?, category: Int?): Flow<PagingData<Product>>

}