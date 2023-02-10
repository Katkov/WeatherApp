package com.example.weatherapp.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weatherapp.utils.getAssetForecast
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat

@RunWith(AndroidJUnit4::class)
class ForecastModelTest {

    @Test
    fun getSevenHoursAfterCurrent() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val forecast = getAssetForecast(context = context)
        val hours = forecast.getSevenHoursAfterCurrent()
        assertEquals(hours!!.size, 7)
    }
}