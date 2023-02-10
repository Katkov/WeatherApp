package com.example.weatherapp.model

import org.junit.Assert.*

import org.junit.Test
import java.text.SimpleDateFormat

class ForecastdayTest {

    @Test
    fun getDayOfWeek() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        val date = dateFormat.parse("2023-02-09 19:55")
        val forecastDay = Forecastday(date = date)
        val dayOfWeek = forecastDay.dayOfWeek
        assertEquals(dayOfWeek, "Thu")
    }

    @Test
    fun getDayOfWeekError() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        val date = dateFormat.parse("2023-02-09 19:55")
        val forecastDay = Forecastday(date = date)
        val dayOfWeek = forecastDay.dayOfWeek
        assertNotEquals(dayOfWeek, "Wed")
    }
}