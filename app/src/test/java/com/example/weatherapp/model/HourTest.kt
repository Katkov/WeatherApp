package com.example.weatherapp.model

import org.junit.Assert.*

import org.junit.Test
import java.text.SimpleDateFormat

class HourTest {

    @Test
    fun getHourValue() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        val date = dateFormat.parse("2023-02-06 04:00")
        val hour = Hour(time = date)
        val hourValue = hour.hourValue
        assertEquals(hourValue, "04")
    }
}