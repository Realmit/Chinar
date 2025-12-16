package com.oookraton.chinarDebug

object DateData {
    // Праздники, сан. дни, выходные в кафе:
    val unavailableDates = listOf(
        DateItem(2025,12,31), // Новый год
        DateItem(2025,2,23), // Праздник
        DateItem(2025,11,12) // Санитарный день
    )
    // Зарезервированные даты:
    val reservedDates = mutableListOf<DateItem>().apply {

    }
}