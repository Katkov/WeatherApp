package com.example.weatherapp.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.provider.FakeDispatcherProvider
import com.example.weatherapp.repository.FakeRepository
import com.example.weatherapp.ui.theme.WeatherAppTheme
import org.junit.Rule
import org.junit.Test

class SearchScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = MainViewModel(FakeRepository(), FakeDispatcherProvider())

    @Test
    fun myTest() {
        // Start the app
        composeTestRule.setContent {
            WeatherAppTheme {
                SearchScreen(mainViewModel = viewModel, onShowForecast = {})
            }
        }

        composeTestRule.onNodeWithText("Type more than 3 letters to start search")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Type City Name here...")
            .assertIsDisplayed()

    }
}