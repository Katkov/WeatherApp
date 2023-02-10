package com.example.weatherapp.repository

import com.example.weatherapp.model.City
import com.example.weatherapp.model.ForecastModel

interface Repository {

    suspend fun fetchCities(query: String): List<City>

    suspend fun fetchForecast(query: String, days: String): ForecastModel
}