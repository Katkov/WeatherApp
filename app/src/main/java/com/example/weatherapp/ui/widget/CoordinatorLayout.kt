package com.example.weatherapp.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun CoordinatorLayout() {
    val density = LocalDensity.current
    val maxToolbarHeightDp = 220.dp
    val maxToolbarHeightPx =
        with(density) { maxToolbarHeightDp.roundToPx().toFloat() }
    val minToolbarHeightDp = 60.dp
    val toolbarDeltaPx = with(density) {
        (maxToolbarHeightDp - minToolbarHeightDp).roundToPx().toFloat()
    }
    val listContentHeightPx = with(density) {
        (50.dp * 100).roundToPx().toFloat()
    }
    val maxScrollOffset = remember { mutableStateOf(0f) }

    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val toolbarOffsetHeightDp =
        with(density) { toolbarOffsetHeightPx.value.toDp() }
    val scrollOffsetHeightPx = remember { mutableStateOf(0f) }
    val alpha: Float = (toolbarDeltaPx + toolbarOffsetHeightPx.value) / toolbarDeltaPx
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val postScrollOffset = scrollOffsetHeightPx.value + available.y
                scrollOffsetHeightPx.value = postScrollOffset.coerceIn(maxScrollOffset.value, 0f)
                if (available.y < 0.0f) {
                    val newOffset = toolbarOffsetHeightPx.value + available.y
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarDeltaPx, 0f)
                } else if (available.y > 0.0f &&
                    scrollOffsetHeightPx.value >= toolbarOffsetHeightPx.value) {
                    toolbarOffsetHeightPx.value = scrollOffsetHeightPx.value
                }
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
            items(100) { index ->
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                    text = "I'm item $index",
                    color = Color.Black)
                Divider(modifier = Modifier.height(1.dp), color = Color.Black)
            }
        }
        TopAppBar(
            modifier = Modifier
                .height(maxToolbarHeightDp + toolbarOffsetHeightDp),
            title = { Text(text = "I'm header", color = Color.Black) },
            elevation = 0.dp
        )
    }
}