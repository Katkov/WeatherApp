package com.example.weatherapp.ui.screen

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ForecastDimensions {
    val headerHeightDp: Dp = 220.dp
    val smallHeaderHeightDp: Dp = 60.dp
    val lazyColumnHeaderHeightDp: Dp = 50.dp
    val lazyColumnHoursSectionHeightDp: Dp = 120.dp
    val lazyColumnDaysSectionItemHeightDp: Dp = 50.dp
    val lazyColumnSquareSectionHeightDp: Dp = 100.dp
    val lazyColumnSectionPaddingDp: Dp = 16.dp
    val numberOfSquareSections: Int = 5

    fun getLazyColumnContentHeightDp(lazyColumnDaysSectionItemsCount : Int) : Dp {
        return lazyColumnHeaderHeightDp + lazyColumnHoursSectionHeightDp + lazyColumnSectionPaddingDp +
                lazyColumnHeaderHeightDp + lazyColumnDaysSectionItemHeightDp * lazyColumnDaysSectionItemsCount + lazyColumnSectionPaddingDp +
                (lazyColumnHeaderHeightDp + lazyColumnSquareSectionHeightDp + lazyColumnSectionPaddingDp) * numberOfSquareSections

    }

    val scrollOffsetToRoundUpFirstSectionHeaderDp: Dp
        get() {
            return lazyColumnHoursSectionHeightDp - lazyColumnHeaderHeightDp / 2
        }

    fun getScrollOffsetToRoundUpSecondSectionHeaderDp(lazyColumnDaysSectionItemsCount: Int): Dp {
        return lazyColumnHoursSectionHeightDp +
                lazyColumnHeaderHeightDp / 2 +
                lazyColumnSectionPaddingDp +
                lazyColumnDaysSectionItemHeightDp * lazyColumnDaysSectionItemsCount
    }

    fun getScrollOffsetToRoundUpThirdSectionHeaderDp(lazyColumnDaysSectionItemsCount: Int): Dp {
        return  lazyColumnHoursSectionHeightDp +
                lazyColumnHeaderHeightDp * 3 / 2 +
                lazyColumnSectionPaddingDp * 2 +
                lazyColumnDaysSectionItemHeightDp * lazyColumnDaysSectionItemsCount +
                lazyColumnSquareSectionHeightDp
    }
}