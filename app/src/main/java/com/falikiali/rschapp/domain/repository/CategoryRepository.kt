package com.falikiali.rschapp.domain.repository

import com.falikiali.rschapp.domain.model.Category
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun getCategories(): Flow<ResultState<List<Category>>>

}