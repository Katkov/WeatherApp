package com.example.weatherapp.utils

import android.content.Context
import com.example.weatherapp.model.City
import com.example.weatherapp.model.ForecastModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException

@Throws(IOException::class)
inline fun <reified T>getAssetJson(context: Context, fileName: String): T {
    val jsonString = context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    val data = object : TypeToken<T>() {}.type
    return getGson().fromJson(jsonString, data)
}

@Throws(IOException::class)
fun getAssetCities(context: Context) : List<City> =
    getAssetJson(context, "Cities.json")


@Throws(IOException::class)
fun getAssetForecast(context: Context) : ForecastModel =
    getAssetJson(context, "Forecast.json")

fun getGson(): Gson =
    GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm")
        .create();


