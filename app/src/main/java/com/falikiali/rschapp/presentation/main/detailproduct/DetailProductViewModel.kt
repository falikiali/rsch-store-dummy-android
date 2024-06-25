package com.falikiali.rschapp.presentation.main.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.model.DetailProduct
import com.falikiali.rschapp.domain.usecase.CartUseCase
import com.falikiali.rschapp.domain.usecase.ProductUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(private val productUseCase: ProductUseCase, private val cartUseCase: CartUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<DetailProduct>>()
    val result: LiveData<ResultState<DetailProduct>> get() = _result

    private val _resultAddCart = MutableLiveData<ResultState<String>>()
    val resultAddCart: LiveData<ResultState<String>> get() = _resultAddCart

    private val _sizeProductSelected = MutableStateFlow("")
    val sizeProductSelected: StateFlow<String> get() = _sizeProductSelected

    fun getProduct(idProduct: String, idCategory: Int) {
        viewModelScope.launch {
            productUseCase.getProduct.invoke(idProduct, idCategory).collect {
                _result.postValue(it)
            }
        }
    }

    fun addCart(idProduct: String) {
        viewModelScope.launch {
            cartUseCase.addProductToCart.invoke(idProduct, sizeProductSelected.value!!).collect {
                _resultAddCart.postValue(it)
            }
        }
    }

    fun updateSizeProductSelected(size: String) {
        _sizeProductSelected.value = size
    }

}