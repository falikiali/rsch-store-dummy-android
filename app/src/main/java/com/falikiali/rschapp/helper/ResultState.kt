package com.falikiali.rschapp.helper

sealed class ResultState<out T: Any> {
    data class Success<T: Any>(val data: T): ResultState<T>()
    data class Failed(val error: String, val code: Int): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}
