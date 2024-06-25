package com.falikiali.rschapp.presentation.authentication.login

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
class LoginViewModel @Inject constructor(private val authUseCase: AuthUseCase, private val preferencesUseCase: PreferencesUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<Auth>>()
    val result: LiveData<ResultState<Auth>> get() = _result

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean> get() = _isEmailValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> get() = _isPasswordValid

    val isBtnLoginEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    private val emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"

    init {
        isBtnLoginEnabled.addSource(isEmailValid) {
            isBtnLoginEnabled.value = it && isPasswordValid.value == true
        }

        isBtnLoginEnabled.addSource(isPasswordValid) {
            isBtnLoginEnabled.value = it && isEmailValid.value == true
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authUseCase.login.invoke(email, password).collect {
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

    fun setAccessToken(token: String) {
        viewModelScope.launch {
            preferencesUseCase.setAccessToken.invoke(token)
        }
    }

}