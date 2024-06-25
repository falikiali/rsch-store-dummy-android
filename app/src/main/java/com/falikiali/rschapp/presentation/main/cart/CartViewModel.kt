package com.falikiali.rschapp.presentation.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.model.ProductCart
import com.falikiali.rschapp.domain.usecase.CartUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartUseCase: CartUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<List<ProductCart>>>()
    val result: LiveData<ResultState<List<ProductCart>>> get() = _result

    private val _resultDeleteProduct = MutableLiveData<ResultState<String>>()
    val resultDeleteProduct: LiveData<ResultState<String>> get() = _resultDeleteProduct

    private val _resultUpdateSelectedProduct = MutableLiveData<ResultState<String>>()
    val resultUpdateSelectedProduct: LiveData<ResultState<String>> get() = _resultUpdateSelectedProduct

    fun getProductInCart() {
        viewModelScope.launch {
            cartUseCase.getProductInCart.invoke().collect {
                _result.postValue(it)
            }
        }
    }

    fun deleteEachProductInCart(idProduct: String) {
        val ids = mutableListOf<String>()
        ids.add(idProduct)

        viewModelScope.launch {
            cartUseCase.deleteProductInCart.invoke(ids).collect {
                _resultDeleteProduct.postValue(it)
            }
        }
    }

    fun updateSelectedProductInCart(id: String, isSelected: Boolean) {
        viewModelScope.launch {
            cartUseCase.updateSelectedProductInCart.invoke(id, isSelected).collect {
                _resultUpdateSelectedProduct.postValue(it)
            }
        }
    }

}

data class ChangedProductCart(
    val idProduct: String,
    val quantity: Int
)