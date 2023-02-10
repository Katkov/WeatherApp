package com.example.weatherapp.networking

import com.example.weatherapp.model.City
import com.example.weatherapp.model.ForecastModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkingService {

    @GET("search.json")
    suspend fun fetchCities(
        @Query("q") query: String,
        @Query("key") key: String = "18d7361d7c9c4c9a9dc213950230102"
    ): List<City>

    @GET("forecast.json")
    suspend fun fetchForecast(
        @Query("q") query: String,
        @Query("days") days: String,
        @Query("api") api: String = "no",
        @Query("alerts") alerts: String = "no",
        @Query("key") key: String = "18d7361d7c9c4c9a9dc213950230102"
    ): ForecastModel

}