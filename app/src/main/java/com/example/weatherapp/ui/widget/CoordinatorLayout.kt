package com.example.weatherapp.ui.widget

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun CoordinatorLayout(bigHeaderContent: @Composable BoxScope.() -> Unit,
                      smallHeaderContent: @Composable AnimatedVisibilityScope.() -> Unit,
                      scrollContent: @Composable ColumnScope.() -> Unit) {
    val density = LocalDensity.current
    val headerHeight = 200.dp
    val headerHeightPx = with(density) { headerHeight.roundToPx().toFloat()}
    val smallHeaderHeight = 80.dp
    val smallHeaderHeightPx = with(density) { smallHeaderHeight.roundToPx().toFloat()}
    val maxHeaderMove = with(density) { (headerHeight - smallHeaderHeight).roundToPx().toFloat()}
    val headerOffsetHeightPx = remember { mutableStateOf(0f) }
    val headerOffsetHeightDp = with(density) { headerOffsetHeightPx.value.toDp()}
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = headerOffsetHeightPx.value + delta * 8
                headerOffsetHeightPx.value = newOffset.coerceIn(-maxHeaderMove, 0f)
                return Offset.Zero
            }
        }
    }
    Column(modifier = Modifier.fillMaxWidth()
        .nestedScroll(nestedScrollConnection)) {
        AnimatedVisibility(visible = headerHeightPx + headerOffsetHeightPx.value > smallHeaderHeightPx) {
            Box (modifier = Modifier.fillMaxWidth()
                                    .height(headerHeight + headerOffsetHeightDp),
                 content = bigHeaderContent) // big header layout
        }
        AnimatedVisibility(visible = headerHeightPx + headerOffsetHeightPx.value <= smallHeaderHeightPx,
                           content = smallHeaderContent) //small header layout
        //Scroll layout
        scrollContent()
    }
}