package com.example.weatherapp.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NoOrErrorSearchResult(message: String? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center),
            text = message ?: "Type more than 3 letters to start search",
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            color = MaterialTheme.colors.primaryVariant)
    }
}