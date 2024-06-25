package com.falikiali.rschapp.data.remote

import com.falikiali.rschapp.data.remote.dto.res.BaseResponse
import com.falikiali.rschapp.data.remote.dto.res.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("product")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("search") search: String?,
        @Query("category") category: Int?
    ): BaseResponse<List<ProductResponse>>

    @GET("product/{idProduct}")
    suspend fun getProduct(@Path("idProduct") idProduct: String): BaseResponse<ProductResponse>

}