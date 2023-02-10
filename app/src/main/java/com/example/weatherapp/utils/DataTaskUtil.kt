package com.example.weatherapp.utils

import com.example.weatherapp.provider.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T>launchDataLoad(viewModelScope: CoroutineScope,
                      dispatcherProvider: DispatcherProvider,
                      mutableStateFlow: MutableStateFlow<NetworkResult<T>>,
                      block: suspend () -> T) {
    mutableStateFlow.value = NetworkResult.Loading
    viewModelScope.launch(dispatcherProvider.IO()) {
        try {
            val t = block()
            mutableStateFlow.value = NetworkResult.Loaded(t)
        } catch (ex: Exception) {
            mutableStateFlow.value = NetworkResult.Error(ex.message ?: "Error loading request")
        }
    }
}