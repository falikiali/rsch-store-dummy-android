package com.falikiali.rschapp.data.remote

import com.falikiali.rschapp.data.remote.dto.req.AddProductToCartRequestBody
import com.falikiali.rschapp.data.remote.dto.req.DeleteProductInCartRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdateProductInCartRequestBody
import com.falikiali.rschapp.data.remote.dto.req.UpdateSelectedProductInCartRequestBody
import com.falikiali.rschapp.data.remote.dto.res.BaseResponse
import com.falikiali.rschapp.data.remote.dto.res.BaseResponseWithoutData
import com.falikiali.rschapp.data.remote.dto.res.ProductCartResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

interface CartApiService {

    @POST("cart")
    suspend fun addProductToCart(@Body body: AddProductToCartRequestBody): BaseResponseWithoutData

    @PUT("cart")
    suspend fun updateProductInCart(@Body body: List<UpdateProductInCartRequestBody>): BaseResponseWithoutData

    @PUT("cart/selected")
    suspend fun updateSelectedProductInCart(@Body body: UpdateSelectedProductInCartRequestBody): BaseResponseWithoutData

    @HTTP(method = "DELETE", path = "cart", hasBody = true)
    suspend fun deleteProductInCart(@Body body: List<DeleteProductInCartRequestBody>): BaseResponseWithoutData

    @GET("cart")
    suspend fun getProductInCart(): BaseResponse<List<ProductCartResponse>>

}