package com.example.weatherapp.ui.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Layout2(toolbar: @Composable () -> Unit,
            list: @Composable () -> Unit) {

}

@Composable
fun Layout(list: @Composable (maxToolbarHeight: Dp, minToolbarHeight: Dp) -> Unit) {
   list(maxToolbarHeight = 220.dp, minToolbarHeight = 60.dp)
}

@Composable
fun CallLayout() {
    Layout{maxToolbarHeight, minToolbarHeight ->
        List(maxToolbarHeight, minToolbarHeight)
    }
}


@Composable
fun ToolBar() {
    TopAppBar(
        modifier = Modifier.height(220.dp),
        title = { Box(modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color.Black)) },
        backgroundColor =  Color.Blue,
        navigationIcon = {
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        }, actions = {
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Share, null)
            }
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Settings, null)
            }
        },
    )
}

@Composable
fun List(maxToolbarHeight: Dp, minToolbarHeight: Dp) {
    val state: LazyListState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .padding(top = minToolbarHeight)
            .onSizeChanged {
                state.layoutInfo.visibleItemsInfo.forEachIndexed { index, itemInfo ->
                    val itemInfoSize = itemInfo.size
                    val itemInfoIndex = itemInfo.index
                    val itemInfoKey = itemInfo.key
                    val itemInfoOffset = itemInfo.offset
                    Log.d("LazyColumnState", "visibleItemsInfo: itemInfoIndex: $itemInfoIndex")
                    Log.d("LazyColumnState", "visibleItemsInfo: itemInfoKey: $itemInfoKey")
                    Log.d("LazyColumnState", "visibleItemsInfo: itemInfoSize: $itemInfoSize")
                    Log.d("LazyColumnState", "visibleItemsInfo: itemInfoOffset: $itemInfoOffset")
                }
                val endOffset = state.layoutInfo.viewportEndOffset
                val startOffset = state.layoutInfo.viewportStartOffset
                val totalItemsCount = state.layoutInfo.totalItemsCount
                val viewPortSize = state.layoutInfo.viewportSize
                val orientation = state.layoutInfo.orientation
                val reverseLayout = state.layoutInfo.reverseLayout
                val beforeContentPadding = state.layoutInfo.beforeContentPadding
                val afterContentPadding = state.layoutInfo.afterContentPadding
                Log.d("LazyColumnState", "viewportStartOffset: $startOffset")
                Log.d("LazyColumnState", "viewportEndOffset: $endOffset")
                Log.d("LazyColumnState", "totalItemsCount: $totalItemsCount")
                Log.d("LazyColumnState", "viewPortSize: $viewPortSize")
                Log.d("LazyColumnState", "orientation: $orientation")
                Log.d("LazyColumnState", "reverseLayout: $reverseLayout")
                Log.d("LazyColumnState", "beforeContentPadding: $beforeContentPadding")
                Log.d("LazyColumnState", "afterContentPadding: $afterContentPadding")
            },
        state = state,
        contentPadding =
        PaddingValues(top = maxToolbarHeight - minToolbarHeight)
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
}

@Composable
fun DimensionSubcomposeExample(minToolbarHeight: Dp = 60.dp, toolbarOffsetHeightDp: Dp = 0.dp) {
    val density = LocalDensity.current
    val maxToolbarHeight = remember { mutableStateOf(0.dp) }
    Box(Modifier.fillMaxSize()) {
        DimensionSubcomposeLayout(
            mainContent = { ToolBar() },
            dependentContent = { size: Size ->
                val dpSize = density.run { size.toDpSize() }
                maxToolbarHeight.value = dpSize.height
                Box(modifier = Modifier.height(maxToolbarHeight.value + toolbarOffsetHeightDp)) {
                    ToolBar()
                }},
            placeMainContent = false
        )
        if (maxToolbarHeight.value == 0.dp) {
            Text(text = "Loading context")
        } else {
            DimensionSubcomposeLayout(
                mainContent = { List(maxToolbarHeight = maxToolbarHeight.value, minToolbarHeight = minToolbarHeight) },
                dependentContent = { size: Size ->
                    val dpSize = density.run { size.toDpSize() }
                    Log.d("List Size", "List Height ${dpSize.height}")
                    List(maxToolbarHeight = maxToolbarHeight.value, minToolbarHeight = minToolbarHeight)
                                   },
                placeMainContent = false
            )

        }
    }
}