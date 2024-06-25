package com.falikiali.rschapp.data.remote

import com.falikiali.rschapp.data.remote.dto.res.BaseResponse
import com.falikiali.rschapp.data.remote.dto.res.CategoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApiService {

    @GET("category")
    suspend fun getCategories(): BaseResponse<List<CategoryResponse>>

}