package com.falikiali.rschapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.usecase.CartUseCase
import com.falikiali.rschapp.domain.usecase.PreferencesUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val preferencesUseCase: PreferencesUseCase, private val cartUseCase: CartUseCase): ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    private val _resultUpdateCart = MutableLiveData<ResultState<String>>()
    val resultUpdateCart: LiveData<ResultState<String>> get() = _resultUpdateCart

    private val _changedProductCart = MutableLiveData<Map<String, Int>>()
    val changedProductCart: LiveData<Map<String, Int>> get() = _changedProductCart

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

    fun updateChangedProduct(products: Map<String, Int>) {
        _changedProductCart.value = products
    }

    fun updateProductInCart(ids: List<String>, quantities: List<Int>) {
        viewModelScope.launch {
            cartUseCase.updateProductInCart.invoke(ids, quantities).collect {
                _resultUpdateCart.postValue(it)
            }
        }
    }

    fun clearChangedProduct() {
        _changedProductCart.value = emptyMap()
    }

}