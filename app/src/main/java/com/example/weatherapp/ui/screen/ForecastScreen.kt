package com.example.weatherapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.model.ForecastModel
import com.example.weatherapp.model.Forecastday
import com.example.weatherapp.model.Hour
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.widget.*
import com.example.weatherapp.utils.NetworkResult
import com.example.weatherapp.utils.getAssetForecast

@Composable
fun ForecastScreen(mainViewModel: MainViewModel) {
    Scaffold {
        when (val state = mainViewModel.forecast.collectAsState().value) {
            is NetworkResult.Empty -> NoOrErrorSearchResult()
            is NetworkResult.Loading -> ProgressView()
            is NetworkResult.Error -> NoOrErrorSearchResult(state.message)
            is NetworkResult.Loaded -> ForecastBody(forecast = state.data)
        }
    }
}

@Composable
fun BigHeaderContent(forecast: ForecastModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.medium),
            text = forecast.location?.name ?: "None",
            color = MaterialTheme.colors.secondary,
            fontSize = MaterialTheme.typography.h5.fontSize
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.medium),
            text = "${forecast.current?.tempC}\u00B0",
            color = MaterialTheme.colors.primaryVariant,
            fontSize = MaterialTheme.typography.h3.fontSize
        )
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = forecast.current?.condition?.iconImg,
            placeholder = DebugPlaceholder(R.drawable.sun),
            contentDescription = "Description"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.medium),
            text = forecast.current?.condition?.text ?: "None",
            color = MaterialTheme.colors.secondary,
            fontSize = MaterialTheme.typography.h6.fontSize
        )
    }
}

@Composable
fun SmallHeaderContent(forecast: ForecastModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.medium),
            text = forecast.location?.name ?: "None",
            color = MaterialTheme.colors.secondary,
            fontSize = MaterialTheme.typography.h5.fontSize
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.alpha(ContentAlpha.medium),
            text = "${forecast.current?.tempC}\u00B0",
            color = MaterialTheme.colors.primaryVariant,
            fontSize = MaterialTheme.typography.h3.fontSize
        )
    }
}

@Composable
fun ScrollContent(forecast: ForecastModel) {
    Spacer(modifier = Modifier.height(32.dp))
    Forecast7Hours(hour7Forecast = forecast.getSevenHoursAfterCurrent())
    Spacer(modifier = Modifier.height(32.dp))
    ForecastDays(days = forecast.forecast?.forecastday)
    Spacer(modifier = Modifier.height(620.dp))
}

@Composable
fun ForecastBody(forecast: ForecastModel) {
    CoordinatorLayout(forecast = forecast) {
        ScrollContent(forecast = forecast)
    }
}

@Composable
fun ForecastDays(days: List<Forecastday>?) {
    if (days == null) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        days.forEach { day ->
            DayCard(day = day)
            Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
        }
    }
}

@Composable
fun DayCard(day: Forecastday) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .height(50.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .weight(1.0f)
                .padding(start = 16.dp),
            text = day.dayOfWeek ?: "None",
            color = MaterialTheme.colors.secondary,
            fontSize = MaterialTheme.typography.h6.fontSize,
        )
        AsyncImage(
            modifier = Modifier.weight(1.0f),
            model = day.day?.condition?.iconImg,
            placeholder = DebugPlaceholder(R.drawable.sun),
            contentDescription = "Description"
        )
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .weight(1.0f),
            text = "${day.day?.mintempC}\u00B0 - ${day.day?.maxtempC}°",
            color = MaterialTheme.colors.secondary,
            fontSize = MaterialTheme.typography.h6.fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Forecast7Hours(hour7Forecast: List<Hour>?) {
    if (hour7Forecast == null) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colors.primaryVariant),
        horizontalArrangement = Arrangement.Center
    ) {
        hour7Forecast.forEach { hour ->
            HourCard(hour = hour)
        }
    }
}

@Composable
fun HourCard(hour: Hour) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Column(
        modifier = Modifier.width(screenWidth / 8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1.0f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                modifier = Modifier
                    .alpha(ContentAlpha.medium),
                text = hour.hourValue ?: "None",
                color = MaterialTheme.colors.secondary,
                fontSize = MaterialTheme.typography.h6.fontSize,
                textAlign = TextAlign.Center
            )
        }
        AsyncImage(
            modifier = Modifier.weight(1.0f),
            model = hour.condition?.iconImg,
            placeholder = DebugPlaceholder(R.drawable.sun),
            contentDescription = "Description"
        )
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .weight(1.0f),
            text = "${hour.tempC}\u00B0",
            color = MaterialTheme.colors.secondary,
            fontSize = MaterialTheme.typography.h6.fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastPreview() {
    WeatherAppTheme {
        Scaffold {
            val forecast = getAssetForecast(context = LocalContext.current)
            ForecastBody(forecast = forecast)
        }
    }
}