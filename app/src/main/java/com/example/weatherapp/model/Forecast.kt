package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList


data class Forecast (

  @SerializedName("forecastday" ) var forecastday : ArrayList<Forecastday> = arrayListOf()

) {
  fun getSevenHoursAfter(current: Date?) : List<Hour>? {
    if (current == null) return null
    val calendar = Calendar.getInstance()
    calendar.time = current
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val hoursSet = mutableListOf<Hour>()
    forecastday.forEach { hoursSet.addAll(it.hour) }
    return hoursSet.filter {
      calendar.time = it.time!!
      val day = calendar.get(Calendar.DAY_OF_MONTH)
      val hour = calendar.get(Calendar.HOUR_OF_DAY)
      day > currentDay || day == currentDay && hour >= currentHour
    }.sortedWith(compareBy{it.time}).take(7)
  }
}