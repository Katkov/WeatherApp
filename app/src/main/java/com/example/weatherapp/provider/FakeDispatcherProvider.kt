package com.example.weatherapp.provider

import kotlinx.coroutines.Dispatchers

class FakeDispatcherProvider : DispatcherProvider {

    override fun IO() = Dispatchers.Unconfined

    override fun Default() = Dispatchers.Unconfined

    override fun Main() = Dispatchers.Unconfined
}