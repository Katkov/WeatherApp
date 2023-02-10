package com.example.weatherapp.model

import org.junit.Test

import org.junit.Assert.*

internal class ConditionTest {

    @Test
    fun getIconImg() {
        val condition = Condition(icon = "//cdn.weatherapi.com/weather/64x64/day/113.png")
        assertEquals("https://cdn.weatherapi.com/weather/64x64/day/113.png", condition.iconImg)
    }
}