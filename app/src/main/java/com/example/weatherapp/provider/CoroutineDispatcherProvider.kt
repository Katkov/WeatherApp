package com.example.weatherapp.provider

import kotlinx.coroutines.Dispatchers

class CoroutineDispatcherProvider: DispatcherProvider {

    override fun IO() = Dispatchers.IO

    override fun Default() = Dispatchers.Default

    override fun Main() = Dispatchers.Main
}