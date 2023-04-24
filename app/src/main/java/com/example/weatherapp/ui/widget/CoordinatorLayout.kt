package com.example.weatherapp.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.state.ToolbarScrollBehavior

@Composable
fun CoordinatorLayout(
    modifier: Modifier = Modifier,
    behavior: ToolbarScrollBehavior,
    toolbarContent: @Composable () -> Unit,
    content: LazyListScope.() -> Unit) {
    val density = LocalDensity.current
    val nestedScrollConnection  = remember { behavior.nestedScrollConnection }
    val state = remember { behavior.state }
    val minToolBarHeightDp = with(density) { state.minToolbarHeightPx.toDp() }
    val maxToolBarHeightDp = with(density) { state.maxToolbarHeightPx.toDp() }
    val toolbarOffsetHeightDp = with(density) { state.toolbarOffsetHeightPx.toDp() }
    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = modifier.padding(top = minToolBarHeightDp ),
            contentPadding = PaddingValues(top = maxToolBarHeightDp - minToolBarHeightDp ),
            content = content)
        Surface(modifier = Modifier.height(maxToolBarHeightDp + toolbarOffsetHeightDp),
                content = toolbarContent)
    }
}