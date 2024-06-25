package com.falikiali.rschapp.data

import android.app.Application
import com.falikiali.rschapp.R
import com.falikiali.rschapp.data.remote.CartApiService
import com.falikiali.rschapp.data.remote.dto.req.AddProductToCartRequestBody
import com.falikiali.rschapp.data.remote.dto.req.DeleteProductInCartRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdateProductInCartRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdateSelectedProductInCartRequestBody
import com.falikiali.rschapp.data.remote.dto.res.BaseErrorResponse
import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.domain.repository.CartRepository
import com.falikiali.rschapp.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApiService: CartApiService, private val application: Application): CartRepository {
    override suspend fun addProductToCart(
        idProduct: String,
        idProductSize: String,
        quantity: Int
    ): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = AddProductToCartRequestBody(idProduct, idProductSize, quantity)

                val response = cartApiService.addProductToCart(body)
                emit(ResultState.Success(response.statusMessage))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data.toString(), err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateProductInCart(
        ids: List<String>,
        quantities: List<Int>
    ): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = mutableListOf<UpdateProductInCartRequestBody>()
                ids.forEachIndexed { idx, id ->
                    body.add(
                        UpdateProductInCartRequestBody(
                            id,
                            quantities[idx]
                        )
                    )
                }
                val response = cartApiService.updateProductInCart(body)
                emit(ResultState.Success(response.statusMessage))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data.toString(), err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateSelectedProductInCart(
        idCart: String,
        isSelected: Boolean
    ): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = UpdateSelectedProductInCartRequestBody(idCart, isSelected)
                val response = cartApiService.updateSelectedProductInCart(body)

                emit(ResultState.Success(response.statusMessage))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data.toString(), err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteProductInCart(ids: List<String>): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val body = mutableListOf<DeleteProductInCartRequestBody>()

                ids.forEach {
                    body.add(DeleteProductInCartRequestBody(it))
                }

                val response = cartApiService.deleteProductInCart(body)
                emit(ResultState.Success(response.statusMessage))
            } catch (e: HttpException) {
                e.response()?.errorBody().let {
                    val err = Gson().fromJson(it?.charStream(), BaseErrorResponse::class.java)

                    if (err.statusCode == 400) {
                        if (err.data is String) {
                            emit(ResultState.Failed(err.data, err.statusCode))
                        } else {
                            emit(ResultState.Failed(application.getString(R.string.bad_request), err.statusCode))
                        }
                    } else {
                        if (err.data != null) {
                            emit(ResultState.Failed(err.data.toString(), err.statusCode))
                        } else {
                            emit(ResultState.Failed(err.statusMessage, err.statusCode))
                        }
                    }
                }
            } catch (e: IOException) {
                emit(ResultState.Failed(application.getString(R.string.connection_error), 0))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getProductInCart(): Flow<ResultState<List<ProductCart>>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val response = cartApiService.getProductInCart()

                val data = response.data.map {
                    ProductCart(
                        it.id,
                        it.idProduct,
                        it.idProductSize,
                        it.productName,
                        it.productImage,
                        it.productSize,
                        it.productStock,
                        it.isSelected,
                        it.quantity,
                        it.totalPrice
                    )
                }
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
}