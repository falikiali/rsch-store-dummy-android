package com.falikiali.rschapp.presentation.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.usecase.AuthUseCase
import com.falikiali.rschapp.domain.usecase.PreferencesUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authUseCase: AuthUseCase, private val preferencesUseCase: PreferencesUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<String>>()
    val result: LiveData<ResultState<String>> get() = _result

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout.invoke().collect {
                _result.postValue(it)
            }
        }
    }

    fun clearData() {
        viewModelScope.launch {
            preferencesUseCase.clearData.invoke()
        }
    }
}