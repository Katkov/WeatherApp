package com.example.weatherapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.model.City
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.widget.NoOrErrorSearchResult
import com.example.weatherapp.ui.widget.ProgressView
import com.example.weatherapp.utils.NetworkResult
import com.example.weatherapp.utils.getAssetCities

@Composable
fun SearchScreen(mainViewModel: MainViewModel, onShowForecast: (String) -> Unit) {
    val searchTextState by mainViewModel.searchTextState
    Scaffold(topBar = {
        SearchBar(text = searchTextState,
                  onTextChange = {
                      mainViewModel.fetchCities(it)
                  })
    }) {
        when (val state = mainViewModel.cities.collectAsState().value) {
            is NetworkResult.Empty -> NoOrErrorSearchResult()
            is NetworkResult.Loading -> ProgressView()
            is NetworkResult.Error -> NoOrErrorSearchResult(state.message)
            is NetworkResult.Loaded -> CityList(cities = state.data,
                                                onShowForecast = onShowForecast)
        }
    }
}

@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Type City Name here...",
                    color = MaterialTheme.colors.secondary
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = MaterialTheme.colors.secondary
            ),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colors.secondary
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colors.secondary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
    }
}

@Composable
fun CityList(cities: List<City>, onShowForecast: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
    ) {
        item {
            Text(text = "Search results",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primaryVariant)
        }
        items(cities) { city ->
            CityCard(city = city,
                onShowForecast = onShowForecast)
            Divider(color = MaterialTheme.colors.primaryVariant, thickness = 1.dp)
        }
    }
}

@Composable
fun CityCard(city: City, onShowForecast: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable {
                onShowForecast(city.name)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = city.name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = city.country,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        Scaffold(topBar = {
            SearchBar(text = "", onTextChange = {})
        }) {
            val cities = getAssetCities(context = LocalContext.current)
            CityList(cities = cities, onShowForecast = {})
            //NoOrErrorSearchResult()
        }
    }
}
