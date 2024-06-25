package com.falikiali.rschapp.domain.usecase.category

import com.falikiali.rschapp.domain.model.Category
import com.falikiali.rschapp.domain.repository.CategoryRepository
import com.falikiali.rschapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategories @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(): Flow<ResultState<List<Category>>> {
        return categoryRepository.getCategories()
    }

}