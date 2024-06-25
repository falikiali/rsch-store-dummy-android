package com.falikiali.rschapp.presentation.main.updatephonenumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.usecase.ProfileUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePhoneNumberViewModel @Inject constructor(private val profileUseCase: ProfileUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<String>>()
    val result: LiveData<ResultState<String>> get() = _result

    private val _isPhoneNumberValid = MutableLiveData<Boolean>()
    val isPhoneNumberValid: LiveData<Boolean> get() = _isPhoneNumberValid

    fun checkPhoneNumberValid(phoneNumber: String) {
        _isPhoneNumberValid.value = phoneNumber.isNotBlank() && !phoneNumber.startsWith('0')
    }

    fun updatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            profileUseCase.updatePhoneNumber.invoke(phoneNumber).collect {
                _result.postValue(it)
            }
        }
    }

}