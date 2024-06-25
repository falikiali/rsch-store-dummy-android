package com.falikiali.rschapp.presentation.main.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.rschapp.domain.model.Profile
import com.falikiali.rschapp.domain.usecase.ProfileUseCase
import com.falikiali.rschapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val profileUseCase: ProfileUseCase): ViewModel() {

    private val _result = MutableLiveData<ResultState<Profile>>()
    val result: LiveData<ResultState<Profile>> get() = _result

    init {
        getProfile()
    }

    fun getProfile() {
        viewModelScope.launch {
            profileUseCase.getProfile.invoke().collect {
                _result.postValue(it)
            }
        }
    }

}