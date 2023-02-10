package com.example.weatherapp.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weatherapp.utils.getAssetForecast
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat

@RunWith(AndroidJUnit4::class)
class ForecastTest {

    @Test
    fun getSevenHoursAfter() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val forecast = getAssetForecast(context = context)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm");
        val date = dateFormat.parse("2023-02-06 19:55")
        val hours = forecast.forecast!!.getSevenHoursAfter(date)
        assertEquals(hours!!.size, 7)
    }
}