package com.example.weatherapp.ui.state

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun rememberToolBarState(
    minToolbarHeightPx: Float,
    maxToolbarHeightPx: Float
): ToolbarState {
    return remember {
        ToolbarState(
            minToolbarHeightPx,
            maxToolbarHeightPx
        )
    }
}

@Stable
class ToolbarState(
    val minToolbarHeightPx: Float,
    val maxToolbarHeightPx: Float
) {

    var toolbarOffsetHeightPx by mutableStateOf(0.0f)

    var scrollOffsetHeightPx by mutableStateOf(0.0f)

    val toolbarDeltaPx = maxToolbarHeightPx - minToolbarHeightPx
}

@Stable
interface ToolbarScrollBehavior {

    val state: ToolbarState

    val nestedScrollConnection: NestedScrollConnection
}

class ExitUntilCollapsedBehavior(
    override val state: ToolbarState,
) : ToolbarScrollBehavior {
    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val postScrollOffset = state.scrollOffsetHeightPx + consumed.y
                state.scrollOffsetHeightPx = postScrollOffset
                if (consumed.y < 0.0f) {
                    val newOffset = state.toolbarOffsetHeightPx + consumed.y
                    state.toolbarOffsetHeightPx = newOffset.coerceIn(-state.toolbarDeltaPx, 0f)
                }
                if (consumed.y > 0.0f &&
                    state.scrollOffsetHeightPx >= state.toolbarOffsetHeightPx) {
                    state.toolbarOffsetHeightPx = state.scrollOffsetHeightPx
                }
                if (consumed.y >= 0f && available.y > 0f) {
                    // Reset the total content offset to zero when scrolling all the way down.
                    // This will eliminate some float precision inaccuracies.
                    state.scrollOffsetHeightPx = 0f
                    state.toolbarOffsetHeightPx = 0f
                }
//                showBottomAngles1.value = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
//                        scrollOffsetToRoundUpFirstSectionHeaderPx < 0.0f
//                showBottomAngles2.value = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
//                        scrollOffsetToRoundUpSecondSectionHeaderPx < 0.0f
//                showBottomAngles3.value = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
//                        scrollOffsetToRoundUpThirdSectionHeaderPx < 0.0f
//                showBottomAngles4.value = state.scrollOffsetHeightPx + state.toolbarDeltaPx +
//                        scrollOffsetToRoundUpFourthSectionHeaderPx < 0.0f
                //Log.d("onPostScroll", "MaxScrollOffset: ${maxScrollOffset.value}")
                //Log.d("onPostScroll", "consumed.y: ${consumed.y}")
                //Log.d("onPostScroll", "available.y: ${available.y}")
                //Log.d("onPostScroll", "scrollOffset: ${scrollOffsetHeightPx.value}")
                //Log.d("onPostScroll", "toolbarOffset: ${toolbarOffsetHeightPx.value}")
                return Offset.Zero
            }
        }
}