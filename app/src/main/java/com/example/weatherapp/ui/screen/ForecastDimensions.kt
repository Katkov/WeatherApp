package com.example.weatherapp.ui.screen

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ForecastDimensions {
    val lazyColumnTopOffsetDp: Dp = 2.dp
    val lazyColumnHeaderHeightDp: Dp = 50.dp
    val lazyColumnHoursSectionHeightDp: Dp = 120.dp
    val lazyColumnDaysSectionItemHeightDp: Dp = 50.dp
    val lazyColumnSquareSectionHeightDp: Dp = 100.dp
    val lazyColumnSectionPaddingDp: Dp = 16.dp

    val scrollOffsetToRoundUpFirstSectionHeaderDp: Dp
        get() {
            return lazyColumnTopOffsetDp +
                    lazyColumnHoursSectionHeightDp - lazyColumnHeaderHeightDp / 2
        }

    fun getScrollOffsetToRoundUpSecondSectionHeaderDp(lazyColumnDaysSectionItemsCount: Int): Dp {
        return lazyColumnTopOffsetDp +
                lazyColumnHoursSectionHeightDp +
                lazyColumnHeaderHeightDp / 2 +
                lazyColumnSectionPaddingDp +
                lazyColumnDaysSectionItemHeightDp * lazyColumnDaysSectionItemsCount
    }

    fun getScrollOffsetToRoundUpThirdSectionHeaderDp(lazyColumnDaysSectionItemsCount: Int): Dp {
        return lazyColumnTopOffsetDp +
                lazyColumnHoursSectionHeightDp +
                lazyColumnHeaderHeightDp * 3 / 2 +
                lazyColumnSectionPaddingDp * 2 +
                lazyColumnDaysSectionItemHeightDp * lazyColumnDaysSectionItemsCount +
                lazyColumnSquareSectionHeightDp
    }

    fun getScrollOffsetToRoundUpFourthSectionHeaderDp(lazyColumnDaysSectionItemsCount: Int): Dp {
        return lazyColumnTopOffsetDp +
                lazyColumnHoursSectionHeightDp +
                lazyColumnHeaderHeightDp * 5 / 2 +
                lazyColumnSectionPaddingDp * 2 +
                lazyColumnDaysSectionItemHeightDp * lazyColumnDaysSectionItemsCount +
                lazyColumnSquareSectionHeightDp * 2
    }
}