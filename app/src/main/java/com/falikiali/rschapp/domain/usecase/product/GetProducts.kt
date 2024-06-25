package com.falikiali.rschapp.domain.usecase.product

import androidx.paging.PagingData
import com.falikiali.rschapp.domain.model.Product
import com.falikiali.rschapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProducts @Inject constructor(private val productRepository: ProductRepository) {

    operator fun invoke(search: String, category: Int): Flow<PagingData<Product>> {
        var newSearch: String? = search
        var newCategory: Int? = category

        if (search == "") {
            newSearch = null
        }

        if (category == 0) {
            newCategory = null
        }

        return productRepository.getProducts(newSearch, newCategory)
    }

}