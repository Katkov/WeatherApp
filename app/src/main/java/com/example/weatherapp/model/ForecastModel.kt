package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class ForecastModel (

  @SerializedName("location" ) var location : Location? = Location(),
  @SerializedName("current"  ) var current  : Current?  = Current(),
  @SerializedName("forecast" ) var forecast : Forecast? = Forecast()

) {
  fun getSevenHoursAfterCurrent() : List<Hour>? {
    return forecast?.getSevenHoursAfter(location?.localtime)
  }
}