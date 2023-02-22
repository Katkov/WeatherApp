package com.example.weatherapp.ui.screen

object ForecastDimensions {
    const val headerHeight: Int = 220
    const val smallHeaderHeight: Int = 60
    const val lazyColumnHeaderHeight: Int = 50
    const val lazyColumnHoursSectionHeight: Int = 120
    const val lazyColumnDaysSectionItemHeight: Int = 50
    const val lazyColumnSquareSectionHeight: Int = 100
    const val lazyColumnSectionPadding: Int = 16
    const val numberOfSquareSections: Int = 5

    fun getLazyColumnContentHeight(lazyColumnDaysSectionItemsCount : Int) : Int {
        return lazyColumnHeaderHeight + lazyColumnHoursSectionHeight + lazyColumnSectionPadding +
                lazyColumnHeaderHeight + lazyColumnDaysSectionItemHeight * lazyColumnDaysSectionItemsCount + lazyColumnSectionPadding +
                numberOfSquareSections * (lazyColumnHeaderHeight + lazyColumnSquareSectionHeight + lazyColumnSectionPadding)

    }

    val scrollOffsetToRoundUpFirstSectionHeader: Int
        get() {
            return lazyColumnHoursSectionHeight - lazyColumnHeaderHeight / 2
        }

    fun getScrollOffsetToRoundUpSecondSectionHeader(lazyColumnDaysSectionItemsCount: Int): Int {
        return lazyColumnHoursSectionHeight +
                lazyColumnHeaderHeight / 2 +
                lazyColumnSectionPadding +
                lazyColumnDaysSectionItemHeight * lazyColumnDaysSectionItemsCount
    }

    fun getScrollOffsetToRoundUpThirdSectionHeader(lazyColumnDaysSectionItemsCount: Int): Int {
        return  lazyColumnHoursSectionHeight +
                3 * lazyColumnHeaderHeight / 2 +
                2 * lazyColumnSectionPadding +
                lazyColumnDaysSectionItemHeight * lazyColumnDaysSectionItemsCount +
                lazyColumnSquareSectionHeight
    }
}