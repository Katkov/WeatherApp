package com.example.weatherapp.repository

import com.example.weatherapp.model.City
import com.example.weatherapp.model.ForecastModel
import com.example.weatherapp.networking.NetworkingService
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val networkingService: NetworkingService) : Repository {

    override suspend fun fetchCities(query: String): List<City> =
        networkingService.fetchCities(query = query)

    override suspend fun fetchForecast(query: String, days: String): ForecastModel =
        networkingService.fetchForecast(query = query, days = days)

}