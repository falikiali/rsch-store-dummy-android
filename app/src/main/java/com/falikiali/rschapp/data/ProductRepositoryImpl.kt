package com.falikiali.rschapp.data

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.falikiali.rschapp.R
import com.falikiali.rschapp.data.paging.ProductPagingSource
import com.falikiali.rschapp.data.remote.CategoryApiService
import com.falikiali.rschapp.data.remote.ProductApiService
import com.falikiali.rschapp.data.remote.dto.res.BaseErrorResponse
import com.falikiali.rschapp.domain.model.Category
import com.falikiali.rschapp.domain.model.DetailProduct
import com.falikiali.rschapp.domain.model.DetailSizeProduct
import com.falikiali.rschapp.domain.model.Product
import com.falikiali.rschapp.domain.repository.ProductRepository
import com.falikiali.rschapp.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService,
    private val categoryApiService: CategoryApiService,
    private val application: Application
): ProductRepository{
    override suspend fun getProduct(idProduct: String, idCategory: Int): Flow<ResultState<DetailProduct>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val responseDetailProduct = productApiService.getProduct(idProduct)
                val responseCategoryProduct = categoryApiService.getCategories()

                val categoryName = responseCategoryProduct.data[idCategory - 1].name

                val data = DetailProduct(
                    categoryName,
                    responseDetailProduct.data.detailSize!!.map {
                        DetailSizeProduct(
                            it.id,
                            it.size,
                            it.stock
                        )
                    }
                )
                emit(ResultState.Success(data))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.data != null) {
                        emit(ResultState.Failed(err.data.toString(), err.statusCode))
                    } else {
                        emit(ResultState.Failed(err.statusMessage, err.statusCode))
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getProducts(search: String?, category: Int?): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, initialLoadSize = 20, prefetchDistance = 1
            ),
            pagingSourceFactory = {
                ProductPagingSource(productApiService, search, category)
            }
        )
            .flow
            .flowOn(Dispatchers.IO)
    }
}