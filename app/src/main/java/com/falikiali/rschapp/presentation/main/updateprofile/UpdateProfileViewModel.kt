package com.falikiali.rschapp.presentation.main.updateprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.model.UpdateProfile
import com.falikiali.rschapp.domain.usecase.ProfileUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<UpdateProfile>>()
    val result: LiveData<ResultState<UpdateProfile>> get() = _result

    private val _isUsernameValid = MutableLiveData<Boolean>()
    val isUsernameValid: LiveData<Boolean> get() = _isUsernameValid

    private val _isFullnameValid = MutableLiveData<Boolean>()
    val isFullnameValid: LiveData<Boolean> get() = _isFullnameValid

    val isBtnDoneEnabled = MediatorLiveData<Boolean>()

    init {
        isBtnDoneEnabled.addSource(isUsernameValid) {
            isBtnDoneEnabled.value = it && isFullnameValid.value == true
        }

        isBtnDoneEnabled.addSource(isFullnameValid) {
            isBtnDoneEnabled.value = it && isUsernameValid.value == true
        }
    }

    fun checkUsernameValid(username: String) {
        _isUsernameValid.value = username.isNotBlank() && !username.contains(" ") && !username.any { it.isUpperCase() }
    }

    fun checkFullnameValid(fullname: String) {
        _isFullnameValid.value = fullname.isNotBlank()
    }

    fun updateProfile(fullname: String, username: String) {
        viewModelScope.launch {
            profileUseCase.updateProfile.invoke(fullname, username).collect {
                _result.postValue(it)
            }
        }
    }

}