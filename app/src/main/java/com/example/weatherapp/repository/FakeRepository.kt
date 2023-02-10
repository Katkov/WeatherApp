package com.example.weatherapp.repository

import com.example.weatherapp.model.City
import com.example.weatherapp.model.ForecastModel

class FakeRepository(): Repository{
    override suspend fun fetchCities(query: String): List<City> {
        if (query.length > 10) {
            throw Exception("Error loading")
        }
        return listOf()
    }

    override suspend fun fetchForecast(query: String, days: String): ForecastModel {
        if (query.length > 10) {
            throw Exception("Error loading")
        }
        return ForecastModel()
    }
}