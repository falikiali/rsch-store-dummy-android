package com.falikiali.rschapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.usecase.CartUseCase
import com.falikiali.rschapp.domain.usecase.PreferencesUseCase
import com.falikiali.rschapp.helper.ResultState
import com.falikiali.rschapp.presentation.main.cart.ChangedProductCart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesUseCase: PreferencesUseCase, private val cartUseCase: CartUseCase): ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    private val _resultUpdateCart = MutableLiveData<ResultState<String>>()
    val resultUpdateCart: LiveData<ResultState<String>> get() = _resultUpdateCart

    private val _changedProduct = MutableLiveData<List<ChangedProductCart>>(emptyList())

    init {
        isLoggedIn()
    }

    private fun isLoggedIn() {
        viewModelScope.launch {
            preferencesUseCase.isLoggedIn.invoke().collect {
                _isLoggedIn.postValue(it)
            }
        }
    }

    fun updateChangedProduct(products: List<ChangedProductCart>) {
        _changedProduct.value = products
    }

    fun updateProductInCart() {
        viewModelScope.launch {
            if (_changedProduct.value!!.isNotEmpty()) {
                cartUseCase.updateProductInCart.invoke(_changedProduct.value!!.map { it.idProduct }, _changedProduct.value!!.map { it.quantity }).collect {
                    _resultUpdateCart.postValue(it)
                }
            }
        }
    }

    fun clearChangedProduct() {
        _changedProduct.value = emptyList()
    }

}