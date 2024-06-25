package com.falikiali.rschapp.presentation.main.updatepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.usecase.ProfileUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(private val profileUseCase: ProfileUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<String>>()
    val result: LiveData<ResultState<String>> get() = _result

    private val _isOldPasswordValid = MutableLiveData<Boolean>()
    val isOldPasswordValid: LiveData<Boolean> get() = _isOldPasswordValid

    private val _isNewPasswordValid = MutableLiveData<Boolean>()
    val isNewPasswordValid: LiveData<Boolean> get() = _isNewPasswordValid

    private val _isConfirmNewPasswordValid = MutableLiveData<Boolean>()
    val isConfirmNewPasswordValid: LiveData<Boolean> get() = _isConfirmNewPasswordValid

    val isBtnDoneEnabled = MediatorLiveData<Boolean>()

    init {
        isBtnDoneEnabled.addSource(isOldPasswordValid) {
            isBtnDoneEnabled.value = it && _isNewPasswordValid.value == true && _isConfirmNewPasswordValid.value == true
        }

        isBtnDoneEnabled.addSource(_isNewPasswordValid) {
            isBtnDoneEnabled.value = it && isOldPasswordValid.value == true && _isConfirmNewPasswordValid.value == true
        }

        isBtnDoneEnabled.addSource(_isConfirmNewPasswordValid) {
            isBtnDoneEnabled.value = it && isOldPasswordValid.value == true && _isNewPasswordValid.value == true
        }
    }

    fun checkOldPasswordValid(oldPassword: String) {
        _isOldPasswordValid.value = oldPassword.isNotBlank() && oldPassword.length >= 8
    }

    fun checkNewPasswordValid(newPassword: String, oldPassword: String) {
        _isNewPasswordValid.value = newPassword.isNotBlank() && newPassword.length >= 8 && newPassword != oldPassword
    }

    fun checkConfirmNewPasswordValid(confirmNewPassword: String, newPassword: String) {
        _isConfirmNewPasswordValid.value = confirmNewPassword.isNotBlank() && confirmNewPassword.length >= 8 && confirmNewPassword == newPassword && isNewPasswordValid.value == true
    }

    fun updatePassword(oldPassword: String, newPassword: String, confirmNewPassword: String) {
        viewModelScope.launch {
            profileUseCase.updatePassword.invoke(oldPassword, newPassword, confirmNewPassword).collect {
                _result.postValue(it)
            }
        }
    }

}