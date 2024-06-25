package com.falikiali.rschapp.presentation.main.dashboard.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.falikiali.rschapp.domain.model.Category
import com.falikiali.rschapp.domain.usecase.CategoryUseCase
import com.falikiali.rschapp.domain.usecase.ProductUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(private val categoryUseCase: CategoryUseCase, private val productUseCase: ProductUseCase): ViewModel() {

    private val _resultCategories = MutableLiveData<ResultState<List<Category>>>()
    val resultCategories: LiveData<ResultState<List<Category>>> get() = _resultCategories

    private val _selectedCategory = MutableStateFlow(0)
    val selectedCategory: StateFlow<Int> get() = _selectedCategory

    private val _searchProduct = MutableStateFlow("")
    val searchProduct: StateFlow<String> get() = _searchProduct

    private val searchAndCategoryFilterPair = combine(
        _searchProduct,
        _selectedCategory
    ) { search, category ->
        Pair(search, category)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = searchAndCategoryFilterPair.flatMapLatest {
        productUseCase.getProducts.invoke(it.first, it.second).cachedIn(viewModelScope)
    }

    fun getCategories() {
        viewModelScope.launch {
            categoryUseCase.getCategories.invoke().collect {
                _resultCategories.postValue(it)
            }
        }
    }

    fun updateSelectedCategory(category: Int) {
        _selectedCategory.value = category
    }

    fun updateSearchProduct(search: String) {
        _searchProduct.value = search
    }

}