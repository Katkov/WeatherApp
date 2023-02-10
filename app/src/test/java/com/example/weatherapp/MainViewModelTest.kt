package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.provider.FakeDispatcherProvider
import com.example.weatherapp.repository.FakeRepository
import com.example.weatherapp.utils.NetworkResult
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = MainViewModel(FakeRepository(), FakeDispatcherProvider())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchCitiesSuccess() = runTest {
        viewModel.fetchCities("London")
        Truth.assertThat(viewModel.cities.value).isInstanceOf(NetworkResult.Loaded::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchCitiesError() = runTest {
        viewModel.fetchCities("LondonLondon")
        Truth.assertThat(viewModel.cities.value).isInstanceOf(NetworkResult.Error::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchCitiesLessThen3Letters() = runTest {
        viewModel.fetchCities("Lo")
        Truth.assertThat(viewModel.cities.value).isInstanceOf(NetworkResult.Empty::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchForecastSuccess() = runTest {
        viewModel.fetchForecast("London")
        Truth.assertThat(viewModel.forecast.value).isInstanceOf(NetworkResult.Loaded::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchForecastError() = runTest {
        viewModel.fetchForecast("LondonLondon")
        Truth.assertThat(viewModel.forecast.value).isInstanceOf(NetworkResult.Error::class.java)
    }
}