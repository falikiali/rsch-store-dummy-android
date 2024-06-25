package com.falikiali.rschapp.data.remote.dto.res

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @field:SerializedName("status_code") val statusCode: Int,
    @field:SerializedName("status_message") val statusMessage: String,
    @field:SerializedName("data") val data: T,
    val pagination: PaginationResponse? = null
)

data class PaginationResponse(
    val page: Int,
    @field:SerializedName("total_page") val totalPage: Int,
    @field:SerializedName("total_data") val totalData: Int
)

data class BaseResponseWithoutData(
    @field:SerializedName("status_code") val statusCode: Int,
    @field:SerializedName("status_message") val statusMessage: String
)