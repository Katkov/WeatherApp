package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName
import java.util.*


data class Location (

  @SerializedName("name"            ) var name           : String? = null,
  @SerializedName("region"          ) var region         : String? = null,
  @SerializedName("country"         ) var country        : String? = null,
  @SerializedName("lat"             ) var lat            : Double? = null,
  @SerializedName("lon"             ) var lon            : Double? = null,
  @SerializedName("tz_id"           ) var tzId           : String? = null,
  @SerializedName("localtime_epoch" ) var localtimeEpoch : Long?    = null,
  @SerializedName("localtime"       ) var localtime      : Date? = null

)