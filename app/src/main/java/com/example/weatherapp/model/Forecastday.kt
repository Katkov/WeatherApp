package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*


data class Forecastday (

  @SerializedName("date"       ) var date      : Date?         = null,
  @SerializedName("date_epoch" ) var dateEpoch : Long?            = null,
  @SerializedName("day"        ) var day       : Day?            = Day(),
  @SerializedName("astro"      ) var astro     : Astro?          = Astro(),
  @SerializedName("hour"       ) var hour      : ArrayList<Hour> = arrayListOf()

) {
  val dayOfWeek : String? get() {
    if (date == null) return null
    return SimpleDateFormat("EE", Locale.ENGLISH).format(date!!.time)
  }
}