package com.example.weatherapp.ui.state

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun rememberForecastToolbarState(
    minToolbarHeightPx: Float,
    maxToolbarHeightPx: Float,
    scrollOffsetToRoundUpFirstSectionHeaderPx: Float,
    scrollOffsetToRoundUpSecondSectionHeaderPx: Float,
    scrollOffsetToRoundUpThirdSectionHeaderPx: Float,
    scrollOffsetToRoundUpFourthSectionHeaderPx: Float
): ForecastToolbarState {
    return remember {
        ForecastToolbarState(
            minToolbarHeightPx,
            maxToolbarHeightPx,
            scrollOffsetToRoundUpFirstSectionHeaderPx,
            scrollOffsetToRoundUpSecondSectionHeaderPx,
            scrollOffsetToRoundUpThirdSectionHeaderPx,
            scrollOffsetToRoundUpFourthSectionHeaderPx
        )
    }
}

@Stable
class ForecastToolbarState (
    minToolbarHeightPx: Float,
    maxToolbarHeightPx: Float,
    val scrollOffsetToRoundUpFirstSectionHeaderPx: Float,
    val scrollOffsetToRoundUpSecondSectionHeaderPx: Float,
    val scrollOffsetToRoundUpThirdSectionHeaderPx: Float,
    val scrollOffsetToRoundUpFourthSectionHeaderPx: Float
): ToolbarState(minToolbarHeightPx, maxToolbarHeightPx) {
    var showBottomAngles1 by mutableStateOf(false)
    var showBottomAngles2 by mutableStateOf(false)
    var showBottomAngles3 by mutableStateOf(false)
    var showBottomAngles4 by mutableStateOf(false)
    var alpha by mutableStateOf(1.0f)
}

class ForecastCollapsedBehavior(
    override val state: ForecastToolbarState
) : ToolbarScrollBehavior {
    override var nestedScrollConnection = ForecastNestedScrollConnection(state)
}

class ForecastNestedScrollConnection(private val state: ForecastToolbarState):
    ExitUntilCollapsedNestedScrollConnection(state) {
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val result = super.onPostScroll(consumed, available, source)
        state.showBottomAngles1 = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
                state.scrollOffsetToRoundUpFirstSectionHeaderPx < 0.0f
        state.showBottomAngles2 = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
                state.scrollOffsetToRoundUpSecondSectionHeaderPx < 0.0f
        state.showBottomAngles3 = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
                state.scrollOffsetToRoundUpThirdSectionHeaderPx < 0.0f
        state.showBottomAngles4 = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
                state.scrollOffsetToRoundUpFourthSectionHeaderPx < 0.0f
        state.alpha = (state.toolbarDeltaPx + state.toolbarOffsetHeightPx) / state.toolbarDeltaPx
        return result
    }
}