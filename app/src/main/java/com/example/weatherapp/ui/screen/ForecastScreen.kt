package com.example.weatherapp.ui.screen

import android.util.Log
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
import androidx.compose.ui.layout.onGloballyPositioned
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
fun ToolbarContent(forecast: ForecastModel, alpha: Float) {
    val oppositeAlpha: Float = 1.0f - alpha
    val density = LocalDensity.current
    val middleMargin = with(density) { 8.dp.roundToPx() }
    val smallMargin = with(density) { 4.dp.roundToPx() }
    val locationOffset = with(density) { 8.dp.roundToPx() }
    val locationFontSize = MaterialTheme.typography.h5.fontSize
    val locationHeight = with(density) { locationFontSize.roundToPx() }
    val temperatureOffset = locationHeight + smallMargin
    val temperatureFontSize = MaterialTheme.typography.h1.fontSize
    val temperatureHeight = with(density) { temperatureFontSize.roundToPx() }
    val imageOffset =
        locationHeight + temperatureHeight + 2 * middleMargin + smallMargin
    val imageSize = 32.dp
    val imageHeight = with(density) { imageSize.roundToPx() }
    val conditionOffset = imageOffset + imageHeight + middleMargin
    val conditionSize = MaterialTheme.typography.subtitle1.fontSize
    val temperatureOffset2 = locationOffset + locationHeight + smallMargin
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
                .offset { IntOffset(0, temperatureOffset) }
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
                .height(ForecastDimensions.lazyColumnHeaderHeightDp)
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
        .height(ForecastDimensions.lazyColumnSquareSectionHeightDp)
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
    val maxToolbarHeightDp = ForecastDimensions.headerHeightDp
    val maxToolbarHeightPx =
        with(density) { maxToolbarHeightDp.roundToPx().toFloat() }
    val minToolbarHeightDp = ForecastDimensions.smallHeaderHeightDp
    val toolbarDeltaPx = with(density) {
        (maxToolbarHeightDp - minToolbarHeightDp).roundToPx().toFloat()
    }
    val listDaysSectionItemsCount = forecast.forecast?.forecastday?.size ?: 0
    val listContentHeight = ForecastDimensions.getLazyColumnContentHeightDp(listDaysSectionItemsCount)
    val listContentHeightPx = with(density) {listContentHeight.roundToPx().toFloat()}
    val maxScrollOffset = remember { mutableStateOf(0f) }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val toolbarOffsetHeightDp =
        with(density) { toolbarOffsetHeightPx.value.toDp() }
    val scrollOffsetHeightPx = remember { mutableStateOf(0f) }
    val alpha: Float = (toolbarDeltaPx + toolbarOffsetHeightPx.value) / toolbarDeltaPx

    //First section visibility dimensions
    val scrollOffsetToRoundUpFirstSectionHeaderPx = with(density) {
        ForecastDimensions.scrollOffsetToRoundUpFirstSectionHeaderDp.roundToPx().toFloat()
    }
    val showBottomAngles1 = remember { mutableStateOf( false) }
    //Second section visibility dimensions
    val scrollOffsetToRoundUpSecondSectionHeaderPx = with(density) {
        ForecastDimensions
            .getScrollOffsetToRoundUpSecondSectionHeaderDp(listDaysSectionItemsCount)
            .roundToPx().toFloat()
    }
    val showBottomAngles2 = remember { mutableStateOf( false) }
    //Third section visibility dimensions
    val scrollOffsetToRoundUpThirdSectionHeaderPx = with(density) {
        ForecastDimensions
            .getScrollOffsetToRoundUpThirdSectionHeaderDp(listDaysSectionItemsCount)
            .roundToPx().toFloat()
    }
    val showBottomAngles3 = remember { mutableStateOf( false) }
    //Forth section visibility dimensions
    val scrollOffsetToRoundUpFourthSectionHeaderPx = with(density) {
        ForecastDimensions
            .getScrollOffsetToRoundUpFourthSectionHeaderDp(listDaysSectionItemsCount)
            .roundToPx().toFloat()
    }
    val showBottomAngles4 = remember { mutableStateOf( false) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                val postScrollOffset = scrollOffsetHeightPx.value + available.y
//                scrollOffsetHeightPx.value = postScrollOffset.coerceIn(maxScrollOffset.value, 0f)
//                Log.d("onPreScroll", "MaxScrollOffset: ${maxScrollOffset.value}")
//                Log.d("onPreScroll", "available.y: ${available.y}")
//                Log.d("onPreScroll", "scrollOffset: ${scrollOffsetHeightPx.value}")
//                if (available.y < 0.0f) {
//                    val newOffset = toolbarOffsetHeightPx.value + available.y
//                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarDeltaPx, 0f)
//                } else if (available.y > 0.0f &&
//                    scrollOffsetHeightPx.value >= toolbarOffsetHeightPx.value) {
//                    toolbarOffsetHeightPx.value = scrollOffsetHeightPx.value
//                }
//                showBottomAngles1.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
//                        scrollOffsetToRoundUpFirstSectionHeaderPx < 0.0f
//                showBottomAngles2.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
//                        scrollOffsetToRoundUpSecondSectionHeaderPx < 0.0f
//                showBottomAngles3.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
//                        scrollOffsetToRoundUpThirdSectionHeaderPx < 0.0f
//                showBottomAngles4.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
//                        scrollOffsetToRoundUpFourthSectionHeaderPx < 0.0f
//                return Offset.Zero
//            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val postScrollOffset = scrollOffsetHeightPx.value + consumed.y + available.y
                scrollOffsetHeightPx.value = postScrollOffset.coerceIn(maxScrollOffset.value, 0f)
                if (consumed.y < 0.0f) {
                    val newOffset = toolbarOffsetHeightPx.value + consumed.y + available.y
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarDeltaPx, 0f)
                } else if (consumed.y > 0.0f &&
                    scrollOffsetHeightPx.value >= toolbarOffsetHeightPx.value) {
                    toolbarOffsetHeightPx.value = scrollOffsetHeightPx.value
                }
                showBottomAngles1.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
                        scrollOffsetToRoundUpFirstSectionHeaderPx < 0.0f
                showBottomAngles2.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
                        scrollOffsetToRoundUpSecondSectionHeaderPx < 0.0f
                showBottomAngles3.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
                        scrollOffsetToRoundUpThirdSectionHeaderPx < 0.0f
                showBottomAngles4.value = scrollOffsetHeightPx.value + toolbarDeltaPx +
                        scrollOffsetToRoundUpFourthSectionHeaderPx < 0.0f
                Log.d("onPostScroll", "MaxScrollOffset: ${maxScrollOffset.value}")
                Log.d("onPostScroll", "consumed.y: ${consumed.y}")
                Log.d("onPostScroll", "available.y: ${available.y}")
                Log.d("onPostScroll", "scrollOffset: ${scrollOffsetHeightPx.value}")
                Log.d("onPostScroll", "toolbarOffset: ${toolbarOffsetHeightPx.value}")
                return Offset.Zero
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val layoutHeight = it.size.height.toFloat()
                maxScrollOffset.value = layoutHeight - maxToolbarHeightPx - listContentHeightPx
            }
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = minToolbarHeightDp ),
            contentPadding =
            PaddingValues(top = maxToolbarHeightDp - minToolbarHeightDp )
        )   {
            item(key = 0) {
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnTopOffsetDp))
            }
            //1st section
            stickyHeader(key = "A") {
                SectionHeader("HOURLY FORECAST", showBottomAngles1.value)
            }

            item(key = 1) {
                Forecast7Hours(hour7Forecast = forecast.getSevenHoursAfterCurrent())
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }

            //2nd section
            stickyHeader(key = "B") {
                SectionHeader("${forecast.forecast?.forecastday?.size}-DAY FORECAST", showBottomAngles2.value)
            }

            item(key = 2) {
                ForecastDays(days = forecast.forecast?.forecastday)
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }

            //3rd section
            stickyHeader(key = "C") {
                CombinedHeader(title1 = "UV INDEX", title2 = "SUNRISE", showBottomAngles = showBottomAngles3.value)
            }

            item(key = 3) {
                CombinedCards(value1 = forecast.current?.uv?.toString() ?: "0",
                    value2 = forecast.forecast?.forecastday?.get(0)?.astro?.sunrise ?: "0:00 AM")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }

            //4th section
            stickyHeader(key = "D") {
                CombinedHeader(title1 = "FEELS LIKE", title2 = "HUMIDITY", showBottomAngles = showBottomAngles4.value)
            }

            item(key = 4) {
                CombinedCards(value1 = "${forecast.current?.feelslikeC}°", value2 = "${forecast.current?.humidity}%")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }

            //5th section
            stickyHeader(key = "E") {
                CombinedHeader(title1 = "WIND", title2 = "PRESSURE", showBottomAngles = false)
            }

            item(key = 5) {
                CombinedCards(value1 = "${forecast.current?.windKph?.toInt()} km/h",
                    value2 = "${forecast.current?.pressureMb?.toInt()} hPa")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }

            //6th section
            stickyHeader(key = "F") {
                CombinedHeader(title1 = "SUNSET", title2 = "MOON RISE", showBottomAngles = false)
            }

            item(key = 6) {
                CombinedCards(value1 = forecast.forecast?.forecastday?.get(0)?.astro?.sunset ?: "0:00 AM",
                    value2 = forecast.forecast?.forecastday?.get(0)?.astro?.moonrise ?: "0:00 AM")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }

            //7th section
            stickyHeader(key = "G") {
                CombinedHeader(title1 = "MOON SET", title2 = "MOON PHASE", showBottomAngles = false)
            }

            item(key = 7) {
                CombinedCards(value1 = forecast.forecast?.forecastday?.get(0)?.astro?.moonset ?: "0:00 AM",
                    value2 = forecast.forecast?.forecastday?.get(0)?.astro?.moonPhase ?: "None")
                Spacer(modifier = Modifier.height(ForecastDimensions.lazyColumnSectionPaddingDp))
            }
        }
        TopAppBar(
            modifier = Modifier
                .height(maxToolbarHeightDp + toolbarOffsetHeightDp),
            title = { ToolbarContent(forecast = forecast, alpha = alpha) },
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
            .height(ForecastDimensions.lazyColumnDaysSectionItemHeightDp)
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
                .height(ForecastDimensions.lazyColumnHeaderHeightDp)
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
            .height(ForecastDimensions.lazyColumnHoursSectionHeightDp)
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
fun ToolbarPreview() {
    WeatherAppTheme {
        val forecast = getAssetForecast(context = LocalContext.current)
        ToolbarContent(forecast = forecast, alpha = 0.8f)
    }
}

@Preview
@Composable
fun SectionHeaderPreview() {
    WeatherAppTheme {
        SectionHeader(title = "HOURLY FORECAST", showBottomAngles = false)
    }
}

@Preview
@Composable
fun SectionHeaderRoundCornersPreview() {
    WeatherAppTheme {
        SectionHeader(title = "HOURLY FORECAST", showBottomAngles = true)
    }
}

@Preview
@Composable
fun HoursSectionPreview() {
    WeatherAppTheme {
        val forecast = getAssetForecast(context = LocalContext.current)
        Forecast7Hours(hour7Forecast = forecast.getSevenHoursAfterCurrent())
    }
}

@Preview
@Composable
fun CombinedHeaderPreview() {
    WeatherAppTheme {
        CombinedHeader(title1 = "SUNSET", title2 = "MOON RISE", showBottomAngles = false)
    }
}

@Preview
@Composable
fun CombinedHeaderRoundCornersPreview() {
    WeatherAppTheme {
        CombinedHeader(title1 = "SUNSET", title2 = "MOON RISE", showBottomAngles = true)
    }
}

@Preview
@Composable
fun CombinedCardsPreview() {
    WeatherAppTheme {
        val forecast = getAssetForecast(context = LocalContext.current)
        CombinedCards(value1 = "${forecast.current?.feelslikeC}°", value2 = "${forecast.current?.humidity}%")
    }
}