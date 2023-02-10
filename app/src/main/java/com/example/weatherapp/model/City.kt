package com.example.weatherapp.model

data class City(val id: Int,
                val name: String,
                val region: String,
                var country: String,
                var lat: Double,
                var lon: Double,
                var url: String)
