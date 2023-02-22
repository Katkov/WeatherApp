package com.example.weatherapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
fun BigHeaderContent(forecast: ForecastModel, alpha: Float) {
    val oppositeAlpha: Float = 1.0f - alpha
    val density = LocalDensity.current
    val middleMargin = with(density) { 8.dp.roundToPx() }
    val smallMargin = with(density) { 4.dp.roundToPx() }
    val locationOffset = with(density) { 8.dp.roundToPx() }
    val locationFontSize = MaterialTheme.typography.h5.fontSize
    val locationHeight = with(density) { locationFontSize.roundToPx() }
    val temperatureFontSize = MaterialTheme.typography.h1.fontSize
    val temperatureHeight = with(density) { temperatureFontSize.roundToPx() }
    val imageOffset = locationHeight + temperatureHeight + 2 * middleMargin
    val imageSize = 32.dp
    val imageHeight = with(density) { imageSize.roundToPx() }
    val conditionOffset = imageOffset + imageHeight + smallMargin
    val conditionSize = MaterialTheme.typography.subtitle1.fontSize
    val temperatureOffset2 = locationOffset + locationHeight
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, locationOffset) },
            text = forecast.location?.name ?: "None",
            color = MaterialTheme.colors.secondary,
            fontSize = locationFontSize
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, temperatureOffset2) }
                .alpha(oppositeAlpha),
            text = "${forecast.current?.tempC?.toInt()}\u00B0 | ${forecast.current?.condition?.text ?: "None"}",
            color = MaterialTheme.colors.primaryVariant,
            fontSize = conditionSize
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, locationHeight) }
                .alpha(alpha),
            text = "${forecast.current?.tempC?.toInt()}\u00B0",
            color = MaterialTheme.colors.primaryVariant,
            fontSize = temperatureFontSize
        )
        AsyncImage(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(size = imageSize)
                .offset { IntOffset(0, imageOffset) }
                .alpha(alpha),
            model = forecast.current?.condition?.iconImg,
            placeholder = DebugPlaceholder(R.drawable.sun),
            contentDescription = "Description"
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, conditionOffset) }
                .alpha(alpha),
            text = forecast.current?.condition?.text ?: "None",
            color = MaterialTheme.colors.secondary,
            fontSize = conditionSize
        )
    }
}

@Composable
fun SmallHeader(modifier: Modifier, title: String, showBottomAngles: Boolean) {
    Box(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            brush = Brush.verticalGradient(
                0.0f to Color.White,
                0.40f to Color.White,
                0.48f to Color.Transparent,
                1.0f to Color.Transparent,
                startY = 0.0f,
                endY = 100.0f
            )
        )) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ForecastDimensions.lazyColumnHeaderHeight.dp)
                .clip(
                    if (showBottomAngles) RoundedCornerShape(20.dp)
                    else RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .background(color = MaterialTheme.colors.primaryVariant)
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                text = title,
                color = MaterialTheme.colors.secondary,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SmallCard(modifier: Modifier, value: String) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(ForecastDimensions.lazyColumnSquareSectionHeight.dp)
        .clip(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        .background(color = MaterialTheme.colors.primaryVariant)) {
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            text = value,
            color = MaterialTheme.colors.background,
            fontSize = MaterialTheme.typography.h5.fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CombinedCards(value1: String, value2: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)) {
        SmallCard(
            Modifier
                .weight(1f)
                .padding(end = 8.dp), value1)
        SmallCard(
            Modifier
                .weight(1f)
                .padding(start = 8.dp), value2)
    }
}

@Composable
fun CombinedHeader(title1 : String, title2: String, showBottomAngles: Boolean) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)) {
        SmallHeader(
            Modifier
                .weight(1f)
                .padding(end = 8.dp),
            title = title1, showBottomAngles = showBottomAngles)
        SmallHeader(
            Modifier
                .weight(1f)
                .padding(start = 8.dp),
            title = title2, showBottomAngles = showBottomAngles)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForecastBody(forecast: ForecastModel) {
    val density = LocalDensity.current
    //Whole activity height
    val layoutHeight = LocalConfiguration.current.screenHeightDp.dp
    val layoutHeightPx = with(density) {layoutHeight.roundToPx().toFloat()}
    //Lazy Column layout content height
    val lazyColumnDaysSectionItemsCount = forecast.forecast?.forecastday?.size ?: 0
    val lazyColumnContentHeight = ForecastDimensions.getLazyColumnContentHeight(lazyColumnDaysSectionItemsCount).dp
    val lazyColumnContentHeightPx = with(density) {lazyColumnContentHeight.roundToPx().toFloat()}
    //Header height
    val headerHeight = ForecastDimensions.headerHeight.dp
    val headerHeightPx = with(density) { headerHeight.roundToPx().toFloat() }
    //Small header height (The header which we see once we scroll up)
    val smallHeaderHeight = ForecastDimensions.smallHeaderHeight.dp
    //Difference between Header and Small Header height
    val maxHeaderMove = with(density) { (headerHeight - smallHeaderHeight).roundToPx().toFloat() }
    //The scrollable height of the Lazy Column
    val approximateScrollMinValue = layoutHeightPx - headerHeightPx - lazyColumnContentHeightPx
    //Current scrollable offset
    val headerOffsetHeightPx = remember { mutableStateOf(0f) }
    val headerOffsetHeightDp = with(density) { headerOffsetHeightPx.value.toDp() }
    val postScrollConsumedY = remember { mutableStateOf(0f) }
    //First section visibility dimensions
    val scrollOffsetToRoundUpFirstSectionHeaderPx = with(density) {
        ForecastDimensions.scrollOffsetToRoundUpFirstSectionHeader.dp.roundToPx().toFloat()
    }
    val showBottomAngles1 = remember { mutableStateOf( false) }
    //Second section visibility dimensions
    val scrollOffsetToRoundUpSecondSectionHeaderPx = with(density) {
        ForecastDimensions
            .getScrollOffsetToRoundUpSecondSectionHeader(lazyColumnDaysSectionItemsCount)
            .dp.roundToPx().toFloat()
    }
    val showBottomAngles2 = remember { mutableStateOf( false) }
    //Third section visibility dimensions
    val scrollOffsetToRoundUpThirdSectionHeaderPx = with(density) {
        ForecastDimensions
            .getScrollOffsetToRoundUpThirdSectionHeader(lazyColumnDaysSectionItemsCount)
            .dp.roundToPx().toFloat()
    }
    val showBottomAngles3 = remember { mutableStateOf( false) }
    //Alpha value for header texts
    val alpha: Float = (maxHeaderMove + headerOffsetHeightPx.value) / maxHeaderMove

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val postScrollOffset = postScrollConsumedY.value + consumed.y
                postScrollConsumedY.value = postScrollOffset.coerceIn(approximateScrollMinValue, 0f)
                if (consumed.y < 0.0f || consumed.y > 0.0f && postScrollConsumedY.value >= headerOffsetHeightPx.value) {
                    val newOffset = headerOffsetHeightPx.value + consumed.y
                    headerOffsetHeightPx.value = newOffset.coerceIn(-maxHeaderMove, 0f)
                }
                showBottomAngles1.value = postScrollConsumedY.value + maxHeaderMove + scrollOffsetToRoundUpFirstSectionHeaderPx < 0.0f
                showBottomAngles2.value = postScrollConsumedY.value + maxHeaderMove + scrollOffsetToRoundUpSecondSectionHeaderPx < 0.0f
                showBottomAngles3.value = postScrollConsumedY.value + maxHeaderMove + scrollOffsetToRoundUpThirdSectionHeaderPx < 0.0f
                return Offset.Zero
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = smallHeaderHeight),
            contentPadding = PaddingValues(top = headerHeight - smallHeaderHeight)
        ) {
            //1st section
            stickyHeader {
                SectionHeader("HOURLY FORECAST", showBottomAngles1.value)
            }

            item {
                Forecast7Hours(hour7Forecast = forecast.getSevenHoursAfterCurrent())
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }

            //2nd section
            stickyHeader {
                SectionHeader("${forecast.forecast?.forecastday?.size}-DAY FORECAST", showBottomAngles2.value)
            }

            item {
                ForecastDays(days = forecast.forecast?.forecastday)
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }

            //3rd section
            stickyHeader {
                CombinedHeader(title1 = "UV INDEX", title2 = "SUNRISE", showBottomAngles = showBottomAngles3.value)
            }

            item {
                CombinedCards(value1 = forecast.current?.uv?.toString() ?: "0",
                    value2 = forecast.forecast?.forecastday?.get(0)?.astro?.sunrise ?: "0:00 AM")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }

            //4th section
            stickyHeader {
                CombinedHeader(title1 = "FEELS LIKE", title2 = "HUMIDITY", showBottomAngles = false)
            }

            item {
                CombinedCards(value1 = "${forecast.current?.feelslikeC}°", value2 = "${forecast.current?.humidity}%")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }

            //5th section
            stickyHeader {
                CombinedHeader(title1 = "WIND", title2 = "PRESSURE", showBottomAngles = false)
            }

            item {
                CombinedCards(value1 = "${forecast.current?.windKph?.toInt()} km/h",
                    value2 = "${forecast.current?.pressureMb?.toInt()} hPa")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }

            //6th section
            stickyHeader {
                CombinedHeader(title1 = "SUNSET", title2 = "MOON RISE", showBottomAngles = false)
            }

            item {
                CombinedCards(value1 = forecast.forecast?.forecastday?.get(0)?.astro?.sunset ?: "0:00 AM",
                    value2 = forecast.forecast?.forecastday?.get(0)?.astro?.moonrise ?: "0:00 AM")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }

            //7th section
            stickyHeader {
                CombinedHeader(title1 = "MOON SET", title2 = "MOON PHASE", showBottomAngles = false)
            }

            item {
                CombinedCards(value1 = forecast.forecast?.forecastday?.get(0)?.astro?.moonset ?: "0:00 AM",
                    value2 = forecast.forecast?.forecastday?.get(0)?.astro?.moonPhase ?: "None")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPadding.dp))
            }
        }
        TopAppBar(
            modifier = Modifier.height(headerHeight + headerOffsetHeightDp),
            title = {
                BigHeaderContent(forecast = forecast, alpha = alpha)
            },
            elevation = 0.dp
        )
    }
}

@Composable
fun ForecastDays(days: List<Forecastday>?) {
    if (days == null) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clip(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(color = MaterialTheme.colors.primaryVariant)
    ) {
        days.forEachIndexed { index, day ->
            DayCard(day = day)
            if (index < days.size - 1) {
                Divider(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    color = MaterialTheme.colors.background, thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun DayCard(day: Forecastday) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .height(ForecastDimensions.lazyColumnDaysSectionItemHeight.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1.0f)
                .padding(start = 16.dp),
            text = day.dayOfWeek ?: "None",
            color = MaterialTheme.colors.background,
            fontSize = MaterialTheme.typography.h6.fontSize,
        )
        AsyncImage(
            modifier = Modifier.weight(1.0f),
            model = day.day?.condition?.iconImg,
            placeholder = DebugPlaceholder(R.drawable.sun),
            contentDescription = "Description"
        )
        Text(
            modifier = Modifier.weight(1.0f),
            text = "${day.day?.mintempC?.toInt()}° - ${day.day?.maxtempC?.toInt()}°",
            color = MaterialTheme.colors.background,
            fontSize = MaterialTheme.typography.h6.fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SectionHeader(title: String, showBottomAngles: Boolean) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            brush = Brush.verticalGradient(
                0.0f to Color.White,
                0.40f to Color.White,
                0.48f to Color.Transparent,
                1.0f to Color.Transparent,
                startY = 0.0f,
                endY = 100.0f
            )
        )) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(ForecastDimensions.lazyColumnHeaderHeight.dp)
                .padding(start = 16.dp, end = 16.dp)
                .clip(
                    if (showBottomAngles) RoundedCornerShape(20.dp)
                    else RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .background(color = MaterialTheme.colors.primaryVariant)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp),
                text = title,
                color = MaterialTheme.colors.secondary,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Forecast7Hours(hour7Forecast: List<Hour>?) {
    if (hour7Forecast == null) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ForecastDimensions.lazyColumnHoursSectionHeight.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clip(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
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
                text = hour.hourValue ?: "None",
                color = MaterialTheme.colors.background,
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
        Box(
            modifier = Modifier.weight(1.0f),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "${hour.tempC?.toInt()}\u00B0",
                color = MaterialTheme.colors.background,
                fontSize = MaterialTheme.typography.h6.fontSize,
                textAlign = TextAlign.Center
            )
        }
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