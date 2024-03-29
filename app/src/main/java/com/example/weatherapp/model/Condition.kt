package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName


data class Condition (

  @SerializedName("text" ) var text : String? = null,
  @SerializedName("icon" ) var icon : String? = null,
  @SerializedName("code" ) var code : Int?    = null

){
  val iconImg get(): String? {
    return if (icon == null) icon else "https:${icon}"
  }
}