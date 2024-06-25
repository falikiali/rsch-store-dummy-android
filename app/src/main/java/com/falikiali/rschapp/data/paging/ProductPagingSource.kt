package com.falikiali.rschapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.falikiali.rschapp.data.remote.ProductApiService
import com.falikiali.rschapp.data.remote.dto.res.ProductResponse
import com.falikiali.rschapp.domain.model.Product
import javax.inject.Inject

class ProductPagingSource @Inject constructor(
    private val productApiService: ProductApiService,
    private val search: String?,
    private val category: Int?
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val page = params.key ?: 1
            val response = productApiService.getProducts(page, search, category)

            LoadResult.Page(
                data = response.data.map {
                    Product(
                        it.id,
                        it.name,
                        it.price,
                        it.description,
                        it.purpose,
                        it.category,
                        it.stock,
                        it.image
                    )
                },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page == response.pagination!!.totalPage) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}