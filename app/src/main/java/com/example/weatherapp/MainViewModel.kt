package com.example.weatherapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.weatherapp.model.City
import com.example.weatherapp.model.ForecastModel
import com.example.weatherapp.provider.DispatcherProvider
import com.example.weatherapp.utils.NetworkResult
import com.example.weatherapp.repository.Repository
import com.example.weatherapp.utils.launchDataLoad
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

const val NUMBER_OF_LETTERS_TO_START_SEARCH = 3
const val NUMBER_OF_DAYS_OF_FORECAST = "10"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _cities = MutableStateFlow<NetworkResult<List<City>>>(NetworkResult.Empty)
    val cities: StateFlow<NetworkResult<List<City>>> = _cities

    private val _forecast = MutableStateFlow<NetworkResult<ForecastModel>>(NetworkResult.Empty)
    val forecast: StateFlow<NetworkResult<ForecastModel>> = _forecast

    fun fetchCities(searchTerm: String) {
        _searchTextState.value = searchTerm
        if (searchTerm.length < NUMBER_OF_LETTERS_TO_START_SEARCH) {
            _cities.value = NetworkResult.Empty
            return
        }
        launchDataLoad(viewModelScope, dispatcherProvider, _cities) {
            repository.fetchCities(searchTerm)
        }
    }

    fun fetchForecast(cityName: String) {
        launchDataLoad(viewModelScope, dispatcherProvider, _forecast) {
            repository.fetchForecast(cityName, NUMBER_OF_DAYS_OF_FORECAST)
        }
    }

}