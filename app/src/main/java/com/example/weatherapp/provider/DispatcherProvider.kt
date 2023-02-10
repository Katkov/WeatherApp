package com.example.weatherapp.provider

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun IO() : CoroutineDispatcher
    fun Default() : CoroutineDispatcher
    fun Main() : CoroutineDispatcher
}