package com.falikiali.rschapp.presentation.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.model.Auth
import com.falikiali.rschapp.domain.usecase.AuthUseCase
import com.falikiali.rschapp.domain.usecase.PreferencesUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authUseCase: AuthUseCase, private val preferencesUseCase: PreferencesUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<Auth>>()
    val result: LiveData<ResultState<Auth>> get() = _result

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean> get() = _isEmailValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> get() = _isPasswordValid

    private val _isConfirmPasswordValid = MutableLiveData<Boolean>()
    val isConfirmPasswordValid: LiveData<Boolean> get() = _isConfirmPasswordValid

    val isBtnRegisterEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    private val emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

    init {
        isBtnRegisterEnabled.addSource(isEmailValid) {
            isBtnRegisterEnabled.value = it && isPasswordValid.value == true && isConfirmPasswordValid.value == true
        }

        isBtnRegisterEnabled.addSource(isPasswordValid) {
            isBtnRegisterEnabled.value = it && isEmailValid.value == true && isConfirmPasswordValid.value == true
        }

        isBtnRegisterEnabled.addSource(isConfirmPasswordValid) {
            isBtnRegisterEnabled.value = it && isEmailValid.value == true && isPasswordValid.value == true
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            authUseCase.register.invoke(email, password, confirmPassword).collect {
                _result.postValue(it)
            }
        }
    }

    fun checkEmailValid(email: String) {
        _isEmailValid.value = email.isNotBlank() && email.matches(emailPattern.toRegex())
    }

    fun checkPasswordValid(password: String) {
        _isPasswordValid.value = password.isNotBlank() && password.length >= 8
    }

    fun checkConfirmPasswordValid(confirmPassword: String, password: String) {
        _isConfirmPasswordValid.value = confirmPassword.isNotBlank() && confirmPassword.length >= 8 && isPasswordValid.value == true && confirmPassword == password
    }

    fun setAccessToken(token: String) {
        viewModelScope.launch {
            preferencesUseCase.setAccessToken.invoke(token)
        }
    }

}