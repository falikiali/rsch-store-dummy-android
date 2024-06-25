package com.falikiali.rschapp.domain.usecase

import com.falikiali.rschapp.domain.usecase.category.GetCategories
import javax.inject.Inject

data class CategoryUseCase @Inject constructor(
    val getCategories: GetCategories
)
