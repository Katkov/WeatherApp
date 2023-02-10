package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.screen.ForecastScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.screen.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = Routes.Search.route) {
                    composable(Routes.Search.route) {
                        val mainViewModel: MainViewModel by viewModels()
                        SearchScreen(mainViewModel = mainViewModel) {
                            mainViewModel.fetchForecast(it)
                            navController.navigate(Routes.Forecast.route)
                        }
                    }
                    composable(Routes.Forecast.route) {
                        val mainViewModel: MainViewModel by viewModels()
                        ForecastScreen(mainViewModel = mainViewModel)
                    }
                }
            }
        }
    }

}

sealed class Routes(val route: String) {
    object Search : Routes("search")
    object Forecast : Routes("forecast")
}
