package com.example.weatherapp.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.model.ForecastModel
import com.example.weatherapp.ui.screen.ForecastBody
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.getAssetForecast

@Composable
fun CoordinatorLayout(
    forecast: ForecastModel,
    scrollContent: @Composable LazyItemScope.() -> Unit
) {
    val density = LocalDensity.current
    val headerHeight = 200.dp
    val smallHeaderHeight = 60.dp
    val maxHeaderMove = with(density) { (headerHeight - smallHeaderHeight).roundToPx().toFloat() }
    val headerOffsetHeightPx = remember { mutableStateOf(0f) }
    val headerOffsetHeightDp = with(density) { headerOffsetHeightPx.value.toDp() }
    val postScrollConsumedY = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                postScrollConsumedY.value = postScrollConsumedY.value + consumed.y
                if (consumed.y < 0.0f) {
                    val delta = consumed.y
                    val newOffset = headerOffsetHeightPx.value + delta
                    headerOffsetHeightPx.value = newOffset.coerceIn(-maxHeaderMove, 0f)
                }
                if (consumed.y > 0.0f && postScrollConsumedY.value >= headerOffsetHeightPx.value) {
                    val delta = consumed.y
                    val newOffset = headerOffsetHeightPx.value + delta
                    headerOffsetHeightPx.value = newOffset.coerceIn(-maxHeaderMove, 0f)
                }
                return super.onPostScroll(consumed, available, source)
            }
        }
    }

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
    val alpha : Float = (maxHeaderMove + headerOffsetHeightPx.value) / maxHeaderMove
    val oppositeAlpha : Float = 1.0f - alpha

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(contentPadding = PaddingValues(top = headerHeight)) {
            item {
                scrollContent()
            }
        }
        TopAppBar(
            modifier = Modifier.height(headerHeight + headerOffsetHeightDp),
            title = {
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
            },
            elevation = 0.dp
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